from os import listdir
from os.path import isfile, join
import json
import sys
import matplotlib.pyplot as plt
import seaborn as sns

# Example of data
#   "expId" : "exp-1",
#   "applicationId" : "app-test16-50",
#   "dataSize" : 692,
#   "key": 123
#   "observedData" : [ {
#     "time" : 1639294436325,
#     "version" : 1
#   }, {
#     "time" : 1639294436396,
#     "version" : 2
#   },   ],
#   "type" : "WRITE",
#   "durationMs" : 60222


# data in format of `observedData`
# Only the first occurence of observed version is returned in a result dictionary
# As we're interested in the earliest time it apperead
def filter_to_the_most_recent_version_avaliability(data: list) -> dict:
    result = {}
    for d in data:
        observed_time = d['time']
        if d['version'] in result:
            result[d['version']] = min(result[d['version']], observed_time)
        else:
            result[d['version']] = observed_time

    return result


# Grouping by key
# go through every group and get write operation data and read and plot
def calculate_pbs(data_path: str) -> dict:
    onlyfiles = [f for f in listdir(data_path) if isfile(join(data_path, f))]

    results = {}
    for file in onlyfiles:
        data = json.load(open(data_path + file))
        
        if data['key'] not in results:
            results[data['key']] = []

        results[data['key']].append(data)

    observed_staleness = {}    

    for key, data in results.items():
        write_data_dict = {}
        read_data_list = []

        # splitting all observations into write data and list of read data
        for d in data:
            # with an assumption that only one WRITE op is possible per key
            observedConsistencyData = filter_to_the_most_recent_version_avaliability(d['observedData'])
            if d['type'] == 'WRITE':
                write_data_dict = observedConsistencyData
            else:
                read_data_list.append(observedConsistencyData)
        
        read_data_staleness = []

        # Calculating difference for every read observation. Difference between when the version was written and when the version was avaliable. 
        for read_data in read_data_list:
            res = []
            for v, t in read_data.items():

                if v not in write_data_dict:
                    # Can happen when the value for a key is present from previous run.
                    continue

                t_diff = t - write_data_dict[v]
                if t_diff > 0:
                    print("Exists {d}".format(d=t_diff))
                res.append(t_diff)

            read_data_staleness.append(res)
    
        # read_data_staleness is a list of lists. Every embedded list contains difference observed for particular read 
        # process for the particular keys.  
    
        # flatten list of lists
        observed_staleness[key] = sum(read_data_staleness, [])

    return observed_staleness

# https://towardsdatascience.com/histograms-and-density-plots-in-python-f6bda88f5ac0
def plot_stats(stats: dict):
    for k, stat in stats.items():
          sns.distplot(stat, hist = False, kde = True,
                 kde_kws = {'shade': True, 'linewidth': 1},
                 label = k)

    # plt.legend(prop={'size': 16}, title = 'PBS', bbox_to_anchor=(1.15, 1.2), loc='upper left')
    plt.xlim([-300, 400])
    plt.title('Density Plot for staleness')
    plt.xlabel('Delay (ms)')
    plt.ylabel('Density')
    plt.tight_layout()
    plt.savefig("results.svg")

   # plt.show()
    

if __name__ == "__main__":
    data_path = sys.argv[1]
    # data_path = '/Users/vsobol/personal_projects/PhD/' \
    #             'Raft_Load_capacity_research/raft_loading_research/' \
    #             'experiment_results/experiment_1/data/'
    stats = calculate_pbs(data_path)
    plot_stats(stats)

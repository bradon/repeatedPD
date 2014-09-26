#!/usr/bin/python

import re
import numpy as np

# UTILS


def find_between(s, pre, post=''):
    result = re.search(pre + '(.*)' + post, s)
    return result.group(1)


def remplazar(diccionario, nombre_template):
    f = open(nombre_template)
    template = f.read()
    for key, value in diccionario.items():
        template = template.replace(str(key), str(value))
    return template

# SPECIFICS

TEMPLATE_NAME = 'TEMPLATE.json'
TEMPLATE_PBS = 'TEMPLATE.pbs'


assortment_vector = ['0.0','0.1','0.2','0.3','0.4','0.5','0.6','0.7','0.8','0.9','1.0']
delta_vector = ['0.0','0.1','0.2','0.3','0.4','0.5','0.6','0.7','0.8','0.9','0.95','0.99']
number_of_sets = 3

def create_filenames():
    ans = []
    blueprint = 'DPDALongRun24SEP_set_{}_delta_{}_r_{}'
    for r in assortment_vector:
        for delta in delta_vector:
            for set_number in range(number_of_sets):
                ans.append(blueprint.format(set_number, delta, r))
    return ans

# INFER VARIABLES


def from_file_name_create_dict(filename):
    # determine variables
    # PDAFirstBatch_set_{}_delta_{}_r_{}
    set_name = find_between(filename, 'DPDALongRun24SEP_set_', '_delta_')
    delta = find_between(filename, '_delta_', '_r_')
    r = find_between(filename, '_r_',)
    # now create dictionary
    a = dict()
    a["DELTA"] = delta
    a["R_VALUE"] = r
    a["OUTPUT_FILE_NAME"] = filename + '.csv'
    a["SEED"] = np.random.randint(999999999)
    a["JSON_FILENAME"] = filename + '.json'
    return a


SUB_FILE_NAME = 'submit_pda.sh'
TEMPLATE_LINE = 'qsub {}\n'


def create_sub():
    for filename in create_filenames():
        open(SUB_FILE_NAME, "a").write(TEMPLATE_LINE.format(filename + ".pbs"))


def main():
    for filename in create_filenames():
        diccionario = from_file_name_create_dict(filename)
        json_name = filename + '.json'
        json_file = open(json_name, 'w+')
        json_file.write(remplazar(diccionario, TEMPLATE_NAME))
        print 'Created ' + json_name
        json_file.close()
        pbs_name = filename + '.pbs'
        pbs_file = open(pbs_name, 'w+')
        pbs_file.write(remplazar(diccionario, TEMPLATE_PBS))
        pbs_file.close()
    create_sub()


if __name__ == '__main__':
    main()

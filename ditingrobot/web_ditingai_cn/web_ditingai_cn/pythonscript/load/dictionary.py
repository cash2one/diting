# -*- coding: utf-8 -*-

import json
import random
import sys

import requests

reload(sys)
sys.setdefaultencoding("utf-8")

ONEBOX_HOST = 'http://localhost:9090/'
DICT_URL = ONEBOX_HOST + 'dictionaries'

HEADERS={
     'Content-Type': 'application/json',
     'X-APP-KEY': '!(*@)^#$%)!@&*$)',
     'X-USER-TYPE': 'DITING_USER',
     'X-USER-ID': '41',
     'X-USER-NAME': 'DITING_USER'
}


def headers():
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'SYSTEM',
         'X-USER-ID': '-1',
         'X-USER-NAME': 'SYSTEM'
    }


def init_industry():
   industries =  [
        {
            'name': 'IT/互联网',
            'value': '0',
            'tag': 'INDUSTRY'
        },
        {
            'name': '文化/体育',
            'value': '1',
            'tag': 'INDUSTRY'
        },
        {
            'name': '音乐/娱乐',
            'value': '2',
            'tag': 'INDUSTRY'
        },
        {
            'name': '酒店/餐饮',
            'value': '3',
            'tag': 'INDUSTRY'
        },
        {
            'name': '旅游',
            'value': '4',
            'tag': 'INDUSTRY'
        },
        {
            'name': '贸易/零售',
            'value': '5',
            'tag': 'INDUSTRY'
        },
        {
            'name': '法律/金融/咨询服务',
            'value': '6',
            'tag': 'INDUSTRY'
        },
        {
            'name': '教育',
            'value': '7',
            'tag': 'INDUSTRY'
        },
        {
            'name': '政府/非营利机构',
            'value': '8',
            'tag': 'INDUSTRY'
        },
        {
            'name': '医疗/保健',
            'value': '9',
            'tag': 'INDUSTRY'
        },
        {
            'name': '房地产建筑',
            'value': '10',
            'tag': 'INDUSTRY'
        },
        {
            'name': '加工/制造',
            'value': '11',
            'tag': 'INDUSTRY'
        },
        {
            'name': '其他',
            'value': '12',
            'tag': 'INDUSTRY'
        }
    ]
   return industries

def init_approval():
    status =  [
        {
            'name': '审批未通过',
            'value': '0',
            'tag': 'APPROVAL'
        },
        {
            'name': '审批通过',
            'value': '1',
            'tag': 'APPROVAL'
        }
    ]
    return status

def init_switch():
    status =  [
        {
            'name': '关闭',
            'value': '0',
            'tag': 'SWITCH'
        },
        {
            'name': '开始',
            'value': '1',
            'tag': 'SWITCH'
        }
    ]
    return status


def create_approval():
    status = init_approval()

    for s in status:
        resp = requests.post('%s/' % DICT_URL, headers=headers(), data=json.dumps(s))
        if resp.status_code == 200:
            print resp.json()
        else:
            print resp.content

def create_switch():
    status = init_switch()

    for s in status:
        resp = requests.post('%s/' % DICT_URL, headers=headers(), data=json.dumps(s))
        if resp.status_code == 200:
            print resp.json()
        else:
            print resp.content


def create_industry():
    industries = init_industry()

    for industry in industries:
        resp = requests.post('%s/' % DICT_URL, headers=headers(), data=json.dumps(industry))
        if resp.status_code == 200:
            print resp.json()
        else:
            print resp.content




if __name__ == "__main__":
    create_industry()
    create_approval()
    create_switch()
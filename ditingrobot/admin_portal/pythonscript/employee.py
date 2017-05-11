# -*- coding: utf-8 -*-

import json
import sys

import requests

reload(sys)
sys.setdefaultencoding("utf-8")

ONEBOX_HOST = 'http://localhost:9090/'
# ONEBOX_HOST = 'http://123.56.183.60:9090/'
EMPLOYEE_URL = ONEBOX_HOST + 'employees'

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


def create():
    employee = {
        "userName": "sunhao",
        "password": "1q2w3e4r",
        "realName": "孙昊"
    }
    resp = requests.post('%s/' % EMPLOYEE_URL, headers=headers(), data=json.dumps(employee))
    if resp.status_code == 200:
        print resp.json()
    else:
        print resp.content

def get(id):
    resp = requests.get('%s/%s' % (EMPLOYEE_URL,id), headers=headers())
    if resp.status_code == 200:
        return resp.json()
    else:
        print resp.content


def update():
    employee_id = 1
    employee = get(employee_id)
    print employee

    # 要更换的密码
    # employee["password"] = ''

    resp = requests.put('%s/update' % EMPLOYEE_URL, headers=headers(), data=json.dumps(employee))
    if resp.status_code == 200:
        print resp.json()
    else:
        print resp.content


def login():
    employee = {
        "userName": "sunhao",
        "password": "1q2w3e4r"
    }
    resp = requests.post('%s/login' % EMPLOYEE_URL, headers=HEADERS, data=json.dumps(employee))
    print resp.json()


if __name__ == "__main__":
    create()
    # update()
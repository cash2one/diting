# -*- coding: utf-8 -*-

import json
import sys

import requests

reload(sys)
sys.setdefaultencoding("utf-8")

ONEBOX_HOST = 'http://localhost:9090/'
ACCOUNT_URL = ONEBOX_HOST + 'accounts'

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
    account = {
        "userName": "13630559553",
        "password": "123456",
        "mobile": "13630559553",
        "realName": "日天"
    }
    resp = requests.post('%s/register/mobile' % ACCOUNT_URL, headers=headers(), data=json.dumps(account))
    if resp.status_code == 200:
        print resp.json()
    else:
        print resp.content

def login():
     account = {
        "userName": "13630559553",
        "password": "123456"
    }
     resp = requests.post('%s/login' % ACCOUNT_URL, headers=HEADERS, data=json.dumps(account))
     print resp.json()



if __name__ == "__main__":
    create()
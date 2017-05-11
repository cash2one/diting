# -*- coding: utf-8 -*-
import sys

import requests

reload(sys)
sys.setdefaultencoding("utf-8")

ONEBOX_HOST = 'http://localhost:9090/'
ACCOUNT_URL = ONEBOX_HOST + 'knowledge/update_keys'


def headers():
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'SYSTEM',
        'X-USER-ID': '-1',
        'X-USER-NAME': 'SYSTEM'
    }


def get_headers(account):
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'DITING_USER',
        'X-USER-ID': str(account.get("id")),
        'X-USER-NAME': str(account.get('userName'))
    }


def update_keywords():
    resp = requests.post('%s/' % ACCOUNT_URL, headers=headers())
    if resp.status_code != 200:
        print resp.content


if __name__ == "__main__":
    update_keywords()

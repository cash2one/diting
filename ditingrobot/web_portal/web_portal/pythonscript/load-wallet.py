# -*- coding: utf-8 -*-

import json
import requests

import sys
reload(sys)
sys.setdefaultencoding("utf-8")

ONEBOX_HOST = 'http://localhost:9090/'
WALLET_URL = ONEBOX_HOST + 'wallets'

def get_system_headers():
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'SYSTEM',
        'X-USER-ID': '-1',
        'X-USER-NAME': 'SYSTEM'
    }


def search_master_wallet(wallet_type):
    resp = requests.get('%s/leger-type/%s_MASTER' % (WALLET_URL, wallet_type), headers = get_system_headers())
    if resp.status_code == 200:
        return resp.json().get('id');
    else:
        return None

def create_master_wallet(wallet_type):
    data = {
        'userId': -1,
        'userType': 'SYSTEM',
        'type': wallet_type,
        'currency': 'CNY',
        'status': 'ACTIVE',
        'balance': '0.00',
        'legerType': wallet_type + '_MASTER'
    }

    r = requests.post(WALLET_URL, headers = get_system_headers(), data=json.dumps(data))
    return r.json().get('id')

def credit_master_wallet(credit_request):
    resp = requests.post('%s/credit-master' % WALLET_URL, headers = get_system_headers(), data=json.dumps(credit_request))
    print resp.text

def get_lots_detail(wallet_id):
    resp = requests.get('%s/%s/lots' % (WALLET_URL,wallet_id), headers = get_system_headers())
    print resp.text

def preload_wallet(wallet_type):
    master_wallet_id = search_master_wallet(wallet_type)

    if master_wallet_id is None:
        master_wallet_id = create_master_wallet(wallet_type)

    print 'master account type [%s]' % wallet_type
    print 'master account id [%s]' % master_wallet_id

    if wallet_type == 'DIBI':
        print 'credit DIBI_D lot'
        credit_master_wallet({
            'walletId': master_wallet_id,
            'lotType': 'DIBI_D',
            'amount': 1000000000.00,
            'reason': 'preload',
            'event': 'PRELOAD'
        })

    print 'get lots detail'
    get_lots_detail(master_wallet_id)
def main():
    preload_wallet('DIBI')

if __name__ == "__main__":
    main()

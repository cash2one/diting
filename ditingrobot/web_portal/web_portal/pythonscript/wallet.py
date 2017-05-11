# -*- coding: utf-8 -*-

import json
import random
import sys

import requests

reload(sys)
sys.setdefaultencoding("utf-8")

ACCOUNT_URL = 'http://localhost:9090/enterprise-users'
ONEBOX_HOST = 'http://localhost:9090/'
WALLET_URL = ONEBOX_HOST + 'wallets'
ACCOUNT_URL = ONEBOX_HOST + 'accounts'

def get_system_headers():
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'DITING_USER',
        'X-USER-ID': '600',
        'X-USER-NAME': 'DITING_USER'
    }


def get_master_wallet(wallet_type):
    resp = requests.get('%s/leger-type/%s_MASTER' % (WALLET_URL, wallet_type), headers = get_system_headers())
    return resp.json().get("id")

def get_lots_detail(wallet_id):
    resp = requests.get('%s/%s/lots' % (WALLET_URL,wallet_id), headers = get_system_headers())
    print resp.text

def create_wallet(wallet):
    resp = requests.post(WALLET_URL, headers = get_system_headers(), data=json.dumps(wallet))
    return resp.json()

def credit_wallet(credit_request):
    resp = requests.post('%s/credit' % WALLET_URL, headers = headers(), data=json.dumps(credit_request))
    print resp.status_code

def debit_wallet(debit_request):
    resp = requests.post('%s/debit' % WALLET_URL, headers = get_system_headers(), data=json.dumps(debit_request))
    print resp.text

def get_all_wallet(type):
    resp = requests.get('%s/search?type=%s&count=2000' % (WALLET_URL,type), headers=get_system_headers())
    if resp.status_code == 200:
        wallets = resp.json()
        return wallets
    else:
        return None

def main():
    master_wallet_id = get_master_wallet('DIBI')
    print 'master wallet id [%s]' % master_wallet_id

    # user_id = random.randint(0,10000000)
    # print user_id

    wallet = create_wallet({
        'userId': 36,
        'userType': 'DITING_USER',
        'type': 'DIBI',
        'balance': '0.00',
        'legerType': 'DIBI_PART'
    })

    print wallet
    get_lots_detail(wallet.get("id"))

    wallet_id = wallet.get("id")

    # credit_wallet({
    #     'walletId': wallet_id,
    #     'lotType': 'DIBI_D',
    #     'amount': 100000,
    #     'reason': '开户赠送',
    #     'event':'REGISTER'
    # })

    # credit_wallet({
    #     'walletId': wallet_id,
    #     'lotType': 'MOBI_M',
    #     'amount': 100000,
    #     'reason': '人民币充值',
    #     'event':''
    # })

    print 'get user wallet lots detail'
    get_lots_detail(wallet_id)

    print 'get master wallet lots detail'
    get_lots_detail(master_wallet_id)

    # debit_wallet({
    #     'walletId': wallet_id,
    #     'amount': 10,
    #     'reason': '面试',
    #     'event':''
    # })
    #
    # debit_wallet({
    #     'walletId': wallet_id,
    #     'amount': 40,
    #     'reason': '下载简历',
    #     'event':''
    # })
    #
    # debit_wallet({
    #     'walletId': wallet_id,
    #     'amount': 10000,
    #     'reason': '无节操消费注定失败',
    #     'event':''
    # })

    print 'get user wallet lots detail'
    get_lots_detail(wallet_id)

    print 'get master wallet lots detail'
    get_lots_detail(master_wallet_id)


def credit_wallet_by_mobile(mobile,amount):
    users = get_account_by_mobile(mobile)
    if len(users) > 0:
        wallets = get_wallet_by_user(users[0].get('id'),'DIBI')
        if len(wallets) > 0:
            credit_wallet({
                'walletId': wallets[0].get('id'),
                'lotType': 'DIBI_D',
                'amount': amount,
                'reason': '人民币充值',
                'event':'RMB'
            })

def get_account_by_mobile(mobile):
    resp = requests.get(ACCOUNT_URL+"/search?mobile="+mobile, headers = get_system_headers())
    if resp.status_code == 200:
        return resp.json().get('items')
    else:
        return None

def get_wallet_by_user(user_id,user_type):
    resp = requests.get(WALLET_URL+"/search?userId="+str(user_id)+'&userType='+user_type, headers = get_system_headers())
    if resp.status_code == 200:
        return resp.json()[0]
    else:
        return None

def main2():
    master_wallet_id = get_master_wallet('DIBI')
    print 'master wallet id [%s]' % master_wallet_id

    # user_id = random.randint(0,10000000)
    # print user_id

    wallet = get_wallet_by_user(600,"DITING_USER")

    print wallet
    get_lots_detail(wallet.get("id"))

    wallet_id = wallet.get("id")

    credit_wallet({
        'walletId': wallet_id,
        'lotType': 'DIBI_D',
        'amount': 444,
        'reason': '送钱',
        'event':'送钱'
    })

    # credit_wallet({
    #     'walletId': wallet_id,
    #     'lotType': 'MOBI_M',
    #     'amount': 100000,
    #     'reason': '人民币充值',
    #     'event':''
    # })

    print 'get user wallet lots detail'
    get_lots_detail(wallet_id)

    print 'get master wallet lots detail'
    get_lots_detail(master_wallet_id)

    debit_wallet({
        'walletId': wallet_id,
        'amount': 555,
        'reason': '抢劫11',
        'event':'抢劫11'
    })

    # debit_wallet({
    #     'walletId': wallet_id,
    #     'amount': 40,
    #     'reason': '下载简历',
    #     'event':''
    # })

    # debit_wallet({
    #     'walletId': wallet_id,
    #     'amount': 10000,
    #     'reason': '无节操消费注定失败',
    #     'event':''
    # })

    # print 'get user wallet lots detail'
    # get_lots_detail(wallet_id)

    # print 'get master wallet lots detail'
    # get_lots_detail(master_wallet_id)

def headers():
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'SYSTEM',
        'X-USER-ID': '-1',
        'X-USER-NAME': 'SYSTEM'
    }

def init_wallet():
    mobiles = ['13778917220','18910210871','13501551919','13910528035','13934219883','18818905868','18701424693']
    resp = requests.get('%s/search-all' % ACCOUNT_URL, headers=headers())
    if resp.status_code == 200:
        accounts = resp.json()
        print len(accounts)
        for account in accounts:
            if account.get('mobile') in mobiles:
                continue
            wallet = get_wallet_by_user(account.get('id'),"DITING_USER")
            print wallet.get('id')

            credit_wallet({
                'walletId': wallet.get('id'),
                'lotType': 'DIBI_D',
                'amount': 500,
                'reason': '初始化送流量500条',
                'event':'初始化送流量'
            })


def payment():
    mobiles = ['13778917220','18910210871','13501551919','13910528035','13934219883','18818905868','18701424693']
    for mobile in mobiles:
        account = {}
        resp = requests.get('%s/mobile/%s' % (ACCOUNT_URL, mobile), headers=headers(), data=json.dumps(account))
        if resp.status_code == 200:
            account = resp.json()
            wallet = get_wallet_by_user(account.get('id'),"DITING_USER")
            credit_wallet({
                'walletId': wallet.get('id'),
                'lotType': 'DIBI_D',
                'amount': 50000,
                'reason': '充值50000条',
                'event':'充值50000条'
            })


def modify_wallet_5000():
    wallets = get_all_wallet('DIBI')
    for wallet in wallets:
        if int(wallet.get('balance')) <= 500:
            credit_wallet({
                'walletId': wallet.get('id'),
                'lotType': 'DIBI_D',
                'amount': int(wallet.get('balance')) + 5000,
                'reason': '低于500点的用户，充值5000点',
                'event': '低于500点的用户，充值5000点'
            })


def modify_wallet_500():
    wallets = get_all_wallet('DIBI')
    for wallet in wallets:
        if int(wallet.get('balance')) >= 1000 and int(wallet.get('balance')) <=6000:
            # print wallet.get('balance')
            credit_wallet({
                'walletId': wallet.get('id'),
                'lotType': 'DIBI_D',
                'amount': -500,
                'reason': '多充值500点，抵扣',
                'event': '多充值500点，抵扣'
            })


if __name__ == "__main__":
    # main()
    # main2()
    # credit_wallet_by_mobile('18622604539',0)
    # init_wallet()
    # modify_wallet_5000()
    modify_wallet_500()
# -*- coding: utf-8 -*-
import json
import sys

import requests
import MySQLdb
import datetime
import time

reload(sys)
sys.setdefaultencoding("utf-8")

# ONEBOX_HOST = 'http://101.201.210.144:9090/'
ONEBOX_HOST = 'http://localhost:9090/'
ACCOUNT_URL = ONEBOX_HOST + 'accounts'
COMPANY_URL = ONEBOX_HOST + 'companies'
ROBOT_URL = ONEBOX_HOST + 'robots'
KNOWLEDGE_URL = ONEBOX_HOST + 'knowledge'
WALLET_URL = ONEBOX_HOST + 'wallets'
WECHAT_URL = ONEBOX_HOST + 'wechat'
SYNONYM_URL = ONEBOX_HOST + 'customersynonym'


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


def fetch_all_data(sql):
    db = MySQLdb.connect(host='rm-2zegmgvp9161777t6o.mysql.rds.aliyuncs.com', user='diting', passwd='diting',
                         db='dtrobot', charset="utf8")
    # db='sunhao_test', charset="utf8")
    # db = MySQLdb.connect(host='localhost', user='root', passwd='root', db='db_zsl', charset="utf8")
    cursor = db.cursor()
    try:
        cursor.execute(sql)
        rows = cursor.fetchall()
    except:
        print "error occurred during fetch all data"
    cursor.close()
    db.close()

    return rows


def fetch_one_data(sql):
    db = MySQLdb.connect(host='rm-2zegmgvp9161777t6o.mysql.rds.aliyuncs.com', user='diting', passwd='diting',
                         db='dtrobot', charset="utf8")
    # db='sunhao_test', charset="utf8")
    # db = MySQLdb.connect(host='localhost', user='root', passwd='root', db='db_zsl', charset="utf8")
    cursor = db.cursor()
    try:
        cursor.execute(sql)
        row = cursor.fetchone()
    except:
        print "error occurred during fetch one data"
    cursor.close()
    db.close()

    return row


def delete(sql):
    print 'delete sql = [' + str(sql) + ']'
    db = MySQLdb.connect(host='rm-2zegmgvp9161777t6o.mysql.rds.aliyuncs.com', user='diting', passwd='diting',
                         db='dtrobot', charset="utf8")
    cursor = db.cursor()
    try:
        cursor.execute(sql)
        db.commit()
    except:
        print "error occurred during delete data ,the sql is [" + sql + "]"
        db.rollback()
    cursor.close()
    db.close()


def invoke(mobile):
    account = account_get(mobile)
    account_id = account.get('id')
    company_id = account.get('companyId')

    print 'start delete company ....'
    delete_company = 'delete from company where id = ' + str(company_id)
    delete(delete_company)
    print 'end delete company ....'

    print 'start delete customer_synonym ....'
    delete_synonym = 'delete from customer_synonym where account_id = ' + str(account_id)
    delete(delete_synonym)
    print 'end delete customer_synonym ....'

    print 'start delete invalid_question ....'
    delete_invalid = 'delete from invalid_question where username = ' + str(mobile)
    delete(delete_invalid)
    print 'end delete invalid_question ....'

    print 'start delete message ....'
    delete_message = 'delete from message where user_id = ' + str(account_id)
    delete(delete_message)
    print 'end delete message ....'

    print 'start delete robot ....'
    delete_robot = 'delete from robot where account_id = ' + str(account_id)
    delete(delete_robot)
    print 'end delete robot ....'

    print 'start delete wechat_account ....'
    delete_wechat_account = 'delete from wechat_account where user_name = ' + str(mobile)
    delete(delete_wechat_account)
    print 'end delete wechat_account ....'

    select_wallet = 'select id from wallet where user_id ='+ str(account_id)
    row = fetch_one_data(select_wallet)
    wallet_id = None
    if row is not None :
        wallet_id = row[0]

    print 'start delete wallet_lot ....'
    delete_wallet_lot = 'delete from wallet_lot where wallet_id = ' + str(wallet_id)
    delete(delete_wallet_lot)
    print 'end delete wallet_lot ....'

    print 'start delete wallet ....'
    delete_wallet = 'delete from wallet where user_id = ' + str(account_id)
    delete(delete_wallet)
    print 'end delete wallet ....'

    select_konwledge = 'select id from knowledge where account_id ='+ str(account_id)
    rows = fetch_all_data(select_konwledge.encode('utf-8'))
    for row in rows:
        knowledge_id = row[0]
        knowledge_delete(knowledge_id)

    print 'start delete knowledge ....'
    delete_knowledge = 'delete from knowledge where account_id = ' + str(account_id)
    delete(delete_knowledge)
    print 'end delete knowledge ....'

    print 'start delete account ....'
    delete_account = 'delete from account where id = ' + str(account_id)
    delete(delete_account)
    print 'end delete account ....'


def account_get(mobile):
    account = {}
    resp = requests.get('%s/mobile/%s' % (ACCOUNT_URL, mobile), headers=headers(), data=json.dumps(account))
    if resp.status_code == 200:
        account = resp.json()
    else:
        print resp.content

    return account


def knowledge_delete(knowledge_id):
    requests.put('%s/delete/%s' % (KNOWLEDGE_URL, knowledge_id), headers=headers())


if __name__ == "__main__":
    # invoke('13699223155')
    pass

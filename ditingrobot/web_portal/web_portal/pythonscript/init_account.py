# -*- coding: utf-8 -*-

import json
import sys
import uuid

import requests

reload(sys)
sys.setdefaultencoding("utf-8")

ONEBOX_HOST = 'http://localhost:9090/'
ACCOUNT_URL = ONEBOX_HOST + 'accounts'
COMPANY_URL = ONEBOX_HOST + 'companies'
ROBOT_URL = ONEBOX_HOST + 'robots'

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

def self_headers(account):
    return{
        'Content-Type': 'application/json',
        'X-APP-KEY': '!(*@)^#$%)!@&*$)',
        'X-USER-TYPE': 'DITING_USER',
        'X-USER-ID': str(account.get('id')),
        'X-USER-NAME': str(account.get('userName'))
    }

def create_account(mobile):
    account = {
        "userName": mobile,
        "password": "dt123456",
        "mobile": mobile,
        "verifyCode": '45561846957352359264',
        "claimEnable": "false"
    }
    resp = requests.post('%s/register/mobile' % ACCOUNT_URL, headers=headers(), data=json.dumps(account))
    result = {}
    if resp.status_code == 200:
        result = resp.json()
    else:
        print 'account register fail' + str(mobile)

    return result

def account_get(mobile):
    account = {}
    resp = requests.get('%s/mobile/%s' % (ACCOUNT_URL, mobile), headers=headers(), data=json.dumps(account))
    if resp.status_code == 200:
        account = resp.json()
    else:
        print resp.content

    return account


def account_update(account):
    resp = requests.put('%s/update' % ACCOUNT_URL, headers=self_headers(account), data=json.dumps(account))
    if resp.status_code != 200:
        print resp.content


def create_company(company,account):
    result = {}
    resp = requests.post('%s/' % COMPANY_URL, headers=self_headers(account), data=json.dumps(company))
    if resp.status_code == 200:
        result = resp.json()
    else:
        print resp.content

    return result

def check_company_name(company_name):
    resp = requests.get('%s/check-name/%s' % (COMPANY_URL,company_name), headers=headers())
    if resp.status_code == 400:
        return True
    else:
        return False

def create_robot(robot,account):
    result = {}
    resp = requests.post('%s/save' % ROBOT_URL, headers=self_headers(account), data=json.dumps(robot))
    if resp.status_code == 200:
        result = resp.json()
    else:
        print resp.content

    return result


def read_account():
    company_names = [line.decode('utf-8').strip() for line in open('company2.txt')]
    # 9 + 月 + 日 + number
    mobile = 91215020000
    flag = 0;

    register_list = []
    for name in company_names:

        # print 'check company name start ...'
        if check_company_name(name) == True:
            register_list.append(name)
            continue
        print 'check company name end ...'

        print 'calculate mobile number ...'
        mobile = mobile + 1
        flag = flag + 1

        # print 'create account start ...'
        create_account(mobile)
        account = account_get(mobile)
        print 'create account end ...'
        # print name
        if account.get('id') == None:
            break
        # print 'create company start ...'
        company = {}
        company['name'] = name
        company['industry'] = 12
        company = create_company(company,account)
        print 'create company end ...'

        # print 'update company id in account start ...'
        account['companyId'] = company.get('id')
        account_update(account)
        print 'update company id in account end ...'

        # print 'create robot start ...'
        robot = {}
        robot['name'] = name
        robot['enable'] = 'false'
        robot['shortDomainName'] = str(uuid.uuid1()).replace("-","")
        robot['welcomes'] = u'很高兴为您服务~'
        create_robot(robot,account)
        print 'create robot end ...'

        print 'success process mobile ' + str(mobile) + '.............'

        if int(flag) == 10000:
            print 'over name : ' + str(name)
            break


    print register_list


def read_account1():
    # 9 + 月 + 日 + number
    mobile = 91210182496

    company_name = '孔强'
    # print 'check company name start ...'
    if check_company_name(company_name) == False:
        print 'check company name end ...'

        account = account_get(mobile)

        # print 'create company start ...'
        company = {}
        company['name'] = company_name
        company['industry'] = 12
        company = create_company(company,account)
        print 'create company end ...'

        # print 'update company id in account start ...'
        account['companyId'] = company.get('id')
        account_update(account)
        print 'update company id in account end ...'

        # print 'create robot start ...'
        robot = {}
        robot['name'] = company_name
        robot['enable'] = 'false'
        robot['shortDomainName'] = str(uuid.uuid1()).replace("-","")
        robot['welcomes'] = u'很高兴为您服务~'
        create_robot(robot,account)
        print 'create robot end ...'

    print 'success process mobile ' + str(mobile) + '.............'





if __name__ == "__main__":
    read_account()
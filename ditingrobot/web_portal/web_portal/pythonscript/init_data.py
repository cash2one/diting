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
                         db='ditingrobot', charset="utf8")
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
                         db='ditingrobot', charset="utf8")
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


def parse_account_info():
    accounts = []
    sql = "SELECT sys_user.USERNAME,sys_user.PASSWORD,sys_user.XINGMING,sys_user.EMAIL,sys_user.LAST_LOGIN,sys_user.IP,tb_customer.c_reg_date,tb_customer.c_up_date, tb_customer.c_invitecode FROM sys_user, tb_customer  WHERE sys_user.USERNAME = tb_customer.c_user_name "
    rows = fetch_all_data(sql.encode('utf-8'))

    print 'total account number = ' + str(len(rows))

    print 'start parse account ............'
    fail_rows = []
    for row in rows:
        try:
            account = {}
            user_name = row[0]
            if user_name == "admin" or user_name == "zhouding" or user_name == "pengjunhui" or user_name == "liufei1988" \
                    or user_name == "dtkefu" or user_name == "administretor" or user_name == "liufei" or user_name == "weiyulin":
                continue

            password = row[1]
            real_name = row[2]
            email = row[3]
            last_login_time = row[4]
            ip = row[5]
            created_time = row[6]
            update_time = row[7]
            invitation_code = row[8]

            if user_name is not None:
                account['userName'] = user_name
                account['mobile'] = user_name

            if password is not None:
                account['password'] = password

            if real_name is not None:
                account['realName'] = real_name

            if email is not None:
                account['email'] = email

            if created_time is not None:
                account['createdTime'] = datetime.datetime.strptime(str(created_time), "%Y-%m-%d %H:%M:%S")

            if update_time is not None:
                account['updatedTime'] = datetime.datetime.strptime(str(update_time), "%Y-%m-%d %H:%M:%S")

            if invitation_code is not None:
                account['invitationCode'] = invitation_code

            if last_login_time is not None:
                if last_login_time == "":
                    account['lastLoginDate'] = datetime.datetime.strptime(
                        str(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")), "%Y-%m-%d %H:%M:%S")
                else:
                    account['lastLoginDate'] = datetime.datetime.strptime(str(last_login_time), "%Y-%m-%d %H:%M:%S")

            if ip is not None:
                account['ip'] = ip
            accounts.append(account)
        except:
            fail_rows.appends(row)
    print 'finish parse rows ......'
    return accounts


def process_account():
    accounts = parse_account_info()
    print 'total process account number = ' + str(len(accounts))
    print 'start process account .......'
    fail_account = []
    for account in accounts:
        try:
            last_login_date = account.get("lastLoginDate")
            time_last_array = time.strptime(str(last_login_date), "%Y-%m-%d %H:%M:%S")
            account["lastLoginDate"] = int(time.mktime(time_last_array)) * 1000

            created_time = account.get("createdTime")
            time_create_array = time.strptime(str(created_time), "%Y-%m-%d %H:%M:%S")
            account["createdTime"] = int(time.mktime(time_create_array)) * 1000

            update_time = account.get("updatedTime")
            time_update_array = time.strptime(str(update_time), "%Y-%m-%d %H:%M:%S")
            account["updatedTime"] = int(time.mktime(time_update_array)) * 1000

            account_create(account)
        except:
            fail_account.appends(account)
    print fail_account
    print 'finish process account ..........'


def account_create(account):
    resp = requests.post('%s/' % ACCOUNT_URL, headers=headers(), data=json.dumps(account))
    if resp.status_code != 200:
        print resp.content


def account_get(mobile):
    account = {}
    resp = requests.get('%s/mobile/%s' % (ACCOUNT_URL, mobile), headers=headers(), data=json.dumps(account))
    if resp.status_code == 200:
        account = resp.json()
    else:
        print resp.content

    return account


def account_update(account):
    resp = requests.put('%s/update' % ACCOUNT_URL, headers=headers(), data=json.dumps(account))
    if resp.status_code != 200:
        print resp.content


def process_company():
    resp = requests.get('%s/search-all' % ACCOUNT_URL, headers=headers())
    if resp.status_code == 200:
        accounts = resp.json()

        print "total account number =" + str(len(accounts))

        fail_accounts = []
        print 'start process company ......'
        for account in accounts:
            try:
                company = {}
                mobile = account.get("mobile")

                sql = "select USERNAME,GONGSI,CHANPIN,HANGYE from sys_user where USERNAME = '" + str(mobile) + "'"

                row = fetch_one_data(sql)
                name = row[1]
                product_name = row[2]
                industry = row[3]
                if name is not None:
                    company['name'] = name
                if product_name is not None:
                    company['productName'] = product_name
                if industry is not None and (industry != "机器人" and industry != "高等教育"):
                    company['industry'] = industry
                result = company_create(company)

                account["companyId"] = result.get("id")

                account_update(account)
            except:
                fail_accounts.appends(account)
        print fail_accounts
        print 'finish process company ......'


def company_create(company):
    result = {}
    resp = requests.post('%s/' % COMPANY_URL, headers=headers(), data=json.dumps(company))
    if resp.status_code == 200:
        result = resp.json()
    else:
        print resp.content

    return result


def process_robot():
    resp = requests.get('%s/search-all' % ACCOUNT_URL, headers=headers())
    if resp.status_code == 200:
        accounts = resp.json()

        print "total account number =" + str(len(accounts))

        fail_accounts = []
        print 'start process robot ......'
        for account in accounts:
            try:
                robot = {}
                mobile = account.get("mobile")
                sql = "select c_robot_name,robot_welcome,token,robot_id,isDisable,invalid_answer1," \
                      "invalid_answer2,invalid_answer3,invalid_answer4,invalid_answer5 from tb_cus_robot where c_user_name = '" + str(
                    mobile) + "'"

                row = fetch_one_data(sql)
                name = row[0]
                welcomes = row[1]
                token = row[2]
                unique_id = row[3]
                enable = row[4]
                answer1 = row[5]
                answer2 = row[6]
                answer3 = row[7]
                answer4 = row[8]
                answer5 = row[9]

                if name is not None:
                    robot['name'] = name
                if welcomes is not None:
                    robot['welcomes'] = welcomes
                if unique_id is not None:
                    robot['uniqueId'] = unique_id
                if token is not None:
                    robot['token'] = token
                if enable is not None:
                    if enable == "0":
                        robot['enable'] = "false"
                    else:
                        robot['enable'] = "true"
                if answer1 is not None:
                    robot['invalidAnswer1'] = answer1
                if answer2 is not None:
                    robot['invalidAnswer2'] = answer2
                if answer3 is not None:
                    robot['invalidAnswer3'] = answer3
                if answer4 is not None:
                    robot['invalidAnswer4'] = answer4
                if answer5 is not None:
                    robot['invalidAnswer5'] = answer5

                robot["companyId"] = account.get("companyId")
                robot["accountId"] = account.get("id")
                robot_create(robot)
            except:
                fail_accounts.appends(account)
        print fail_accounts
        print 'finish process robot ......'


def robot_create(robot):
    result = {}
    resp = requests.post('%s/' % ROBOT_URL, headers=headers(), data=json.dumps(robot))
    if resp.status_code == 200:
        result = resp.json()
    else:
        print resp.content

    return result


def process_knowledge():
    print 'start process knowledge repository A .... '
    sql = 'SELECT question,answer,kw1,kw2,kw3,kw4,kw5,cj,emotion,frequency FROM tb_knowledge '
    rows = fetch_all_data(sql)
    print 'total knowledge rows number = ' + str(len(rows))
    for row in rows:
        knowledge = {}
        question = row[0]
        answer = row[1]
        kw1 = row[2]
        kw2 = row[3]
        kw3 = row[4]
        kw4 = row[5]
        kw5 = row[6]
        scene = row[7]
        emotion = row[8]
        frequency = row[9]

        if question is not None:
            knowledge['question'] = question
        if answer is not None:
            knowledge['answer'] = answer
        if kw1 is not None:
            knowledge['kw1'] = kw1
        if kw2 is not None:
            knowledge['kw2'] = kw2
        if kw3 is not None:
            knowledge['kw3'] = kw3
        if kw4 is not None:
            knowledge['kw4'] = kw4
        if kw5 is not None:
            knowledge['kw5'] = kw5
        if scene is not None:
            knowledge['scene'] = scene
        if emotion is not None:
            knowledge['emotion'] = emotion
        if frequency is not None:
            knowledge['frequency'] = frequency

        knowledge["companyId"] = -1
        knowledge["accountId"] = -1

        knowledge_create(knowledge)
    print  'finish process knowledge repository A .... '


def process_account_knowledge():
    resp = requests.get('%s/search-all' % ACCOUNT_URL, headers=headers())
    if resp.status_code == 200:
        accounts = resp.json()
        print "total account number =" + str(len(accounts))

        fail_accounts = []
        print 'start process knowledge ......'
        for account in accounts:
            try:
                table = "tb_" + str(account.get("mobile")) + "_knowledge"
                sql = 'SELECT question,answer,kw1,kw2,kw3,kw4,kw5,cj,emotion,frequency FROM ' + table + ''
                rows = fetch_all_data(sql)

                for row in rows:
                    knowledge = {}
                    question = row[0]
                    answer = row[1]
                    kw1 = row[2]
                    kw2 = row[3]
                    kw3 = row[4]
                    kw4 = row[5]
                    kw5 = row[6]
                    scene = row[7]
                    emotion = row[8]
                    frequency = row[9]

                    if question is not None:
                        knowledge['question'] = question
                    if answer is not None:
                        knowledge['answer'] = answer
                    if kw1 is not None:
                        knowledge['kw1'] = kw1
                    if kw2 is not None:
                        knowledge['kw2'] = kw2
                    if kw3 is not None:
                        knowledge['kw3'] = kw3
                    if kw4 is not None:
                        knowledge['kw4'] = kw4
                    if kw5 is not None:
                        knowledge['kw5'] = kw5
                    if scene is not None:
                        knowledge['scene'] = scene
                    if emotion is not None:
                        knowledge['emotion'] = emotion
                    if frequency is not None:
                        knowledge['frequency'] = frequency

                    knowledge["companyId"] = account.get("companyId")
                    knowledge["accountId"] = account.get("id")

                    knowledge_create(knowledge)
            except:
                fail_accounts.appends(account)
        print fail_accounts
        print 'finish process knowledge ......'


def knowledge_create(knowledge):
    resp = requests.post('%s/' % KNOWLEDGE_URL, headers=headers(), data=json.dumps(knowledge))
    if resp.status_code != 200:
        print resp.content


def wechat_account_create(wechat_account, account):
    resp = requests.post('%s/create' % WECHAT_URL, headers=get_headers(account), data=json.dumps(wechat_account))
    if resp.status_code != 200:
        print resp.content


def customer_synonym_create(customer_synonym, account):
    resp = requests.post('%s/create' % SYNONYM_URL, headers=get_headers(account), data=json.dumps(customer_synonym))
    if resp.status_code != 200:
        print resp.content


def create_wallet(wallet):
    resp = requests.post(WALLET_URL, headers=headers(), data=json.dumps(wallet))
    if resp.status_code != 200:
        print resp.content


def init_wallet():
    resp = requests.get('%s/search-all' % ACCOUNT_URL, headers=headers())
    if resp.status_code == 200:
        accounts = resp.json()
        print 'start create wallet count :' + str(len(accounts))
        for account in accounts:
            # type = dibi
            wallet = {}
            wallet["userId"] = account.get('id')
            wallet["userType"] = u'DITING_USER'
            wallet["type"] = u'DIBI'
            wallet["legerType"] = u'DIBI_PART'
            create_wallet(wallet)
        print 'success create wallet count .......'


def process_wechat_account():
    print 'start process wechat account.... '
    sql = 'SELECT authorizer_appid,authorizer_refresh_token,username,appId FROM authorization_info '
    rows = fetch_all_data(sql)
    print 'total account rows number = ' + str(len(rows))
    for row in rows:
        wechat_account = {}
        original_id = row[0]
        refresh_token = row[1]
        user_name = row[2]
        app_id = row[3]

        if original_id is not None:
            wechat_account['originalId'] = original_id
        if refresh_token is not None:
            wechat_account['refreshToken'] = refresh_token
        if user_name is not None:
            wechat_account['userName'] = user_name
        if app_id is not None:
            wechat_account['appId'] = app_id

        wechat_account_create(wechat_account, account_get(user_name))

    print  'finish process wechat account.... .... '


def process_customer_synonym():
    print 'start process synonym.... '
    sql = 'SELECT word_old,word_new,user_name FROM tb_customer_synonym '
    rows = fetch_all_data(sql)
    print 'total synonym rows number = ' + str(len(rows))
    for row in rows:
        customer_synonym = {}
        word_old = row[0]
        word_new = row[1]
        user_name = row[2]

        if user_name == "liufei":
            continue

        if word_old is not None:
            customer_synonym['word_old'] = word_old
        if word_new is not None:
            customer_synonym['word_new'] = word_new
        if user_name is not None:
            customer_synonym['account_id'] = account_get(user_name).get('id')

        customer_synonym_create(customer_synonym, account_get(user_name))

    print  'finish process synonym .... .... '


if __name__ == "__main__":
    # process_account()
    # process_company()
    # process_robot()
    # process_knowledge()
    # process_account_knowledge()
    init_wallet()
    # process_wechat_account()
    # process_customer_synonym()
    # pass

# -*-coding: utf-8-*-
import socket
import sys
from functools import wraps
from flask import Flask, abort, Blueprint, render_template, request, redirect, session, Response, jsonify,make_response
# from flask import make_response
from werkzeug import filesystem,formparser
from settings import API_URL
from xlwt import Workbook

from common import get_headers, THREAD_LOCAL
from flask_cors import cross_origin
from qiniu import Auth , put_file, etag, urlsafe_base64_encode
import qiniu.config

import requests
import json
import datetime
import time
import os
import re
import qiniu_image
import ssl
ssl._create_default_https_context = ssl._create_unverified_context
reload(sys)
sys.setdefaultencoding("utf-8")

web_ui = Blueprint('web_ui', __name__)
app = Flask(__name__)


def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        # print "request_login  " + request.headers.get('Host')
        # if (request.headers.get('Host').find('ditingai.com')<0):
        #     # resp = jsonify({'message': 'FALSE'})
        #     response = Response()
        #     response.data="false"
        #     response.headers['Access-Control-Allow-Origin'] = "*"
        #     return response
        if (session.get('user_id') is None or session.get('user_type') != u'DITING_USER'):
            response=Response()
            response.headers['Access-Control-Allow-Origin'] = "*"
            return redirect('/login')
        return f(*args, **kwargs)

    return decorated_function


def key_information(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')

        robot = get_robot_info()
        if robot.get('name') is None or robot.get('shortDomainName') is None:
            return redirect('/robot-setting')
        return f(*args, **kwargs)

    return decorated_function


def company_information(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')

        return f(*args, **kwargs)

    return decorated_function


def referrer(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        THREAD_LOCAL.referrer = 'PC'
        return f(*args, **kwargs)

    return decorated_function


@web_ui.route('/')
@referrer
@cross_origin()
def home_page():
    static_path = app.static_folder
    user_agent = str(request.headers.get('User-Agent')).lower()
    if request.base_url.find("antusheng.cn")>=0 or request.base_url.find("ditingai.cc")>=0:
        #return redirect('/m/index')
        return render_template('web/antushengPc1.0/index.html', loginInfo=get_login_info())
    elif re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) and request.base_url.find("ditingai.com") >= 0:
        return redirect('/m')
    elif request.base_url.find("ditingai.cn")>=0:
        return redirect('/m/indexPage')
    elif request.base_url.find("dtrobo.com")>=0:
        return redirect('/dtrobo_index')
    # elif re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent):
    #     return redirect('/robot-company/7818c7f73c31468ca03c63b7375994b9')
    else:
        return render_template('web/ditingpc1.0/index2.html', loginInfo=get_login_info())

@web_ui.route('/new0')
@referrer
@cross_origin()
def ditingNew0():
    return render_template('web/diting_news/ditingNew0.html', loginInfo=get_login_info())

@web_ui.route('/new1')
@referrer
@cross_origin()
def ditingNew1():
    return render_template('web/diting_news/ditingNew1.html')

@web_ui.route('/new2')
@referrer
@cross_origin()
def ditingNew2():
    return render_template('web/diting_news/ditingNew2.html')

@web_ui.route('/new3')
@referrer
@cross_origin()
def ditingNew3():
    return render_template('web/diting_news/ditingNew3.html')

@web_ui.route('/new4')
@referrer
@cross_origin()
def ditingNew4():
    return render_template('web/diting_news/ditingNew4.html')

@web_ui.route('/new5')
@referrer
@cross_origin()
def ditingNew5():
    return render_template('web/diting_news/ditingNew5.html')

@web_ui.route('/new6')
@referrer
@cross_origin()
def ditingNew6():
    return render_template('web/diting_news/ditingNew6.html')

@web_ui.route('/new7')
@referrer
@cross_origin()
def ditingNew7():
    return render_template('web/diting_news/ditingNew7.html')

@web_ui.route('/new8')
@referrer
@cross_origin()
def ditingNew8():
    return render_template('web/diting_news/ditingNew8.html')

@web_ui.route('/new9')
@referrer
@cross_origin()
def ditingNew9():
    return render_template('web/diting_news/ditingNew9.html')

@web_ui.route('/new10')
@referrer
@cross_origin()
def ditingNew10():
    return render_template('web/diting_news/ditingNew10.html')

@web_ui.route('/new11')
@referrer
@cross_origin()
def ditingNew11():
    return render_template('web/diting_news/ditingNew11.html')

@web_ui.route('/new12')
@referrer
@cross_origin()
def ditingNew12():
    return render_template('web/diting_news/ditingNew12.html')

@web_ui.route('/new13')
@referrer
@cross_origin()
def ditingNew13():
    return render_template('web/diting_news/ditingNew13.html')

# @web_ui.route('/new14')
# @referrer
# @cross_origin()
# def ditingNew14():
#     return render_template('web/diting_news/ditingNew14.html')


@web_ui.route('/register')
@referrer
@cross_origin()
def register_mobile():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    user_agent = str(request.headers.get('User-Agent')).lower()
    if request.base_url.find("antusheng.cn") >= 0 or request.base_url.find("ditingai.cc")>=0:
        return redirect('/m/register?yaoqingma=' + invitation_code)
    elif re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) and request.base_url.find("ditingai.com") >= 0:
        return redirect('/m/registerMditing?yaoqingma=' + invitation_code)
    # elif re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent):
    #     return redirect('/m/register?yaoqingma=' + invitation_code)
    else:
        return render_template('web/ditingpc1.0/register.html', invitationCode=invitation_code)


@web_ui.route('/m/register')
@referrer
@cross_origin()
def register_mobile_phone():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/register_phone.html', invitationCode=invitation_code)

@web_ui.route('/m/wks_index')
@referrer
@cross_origin()
def wxs():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/wks.html', invitationCode=invitation_code)

@web_ui.route('/m/wks')
@referrer
@cross_origin()
def wks_index():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/wks_index.html', invitationCode=invitation_code)

@web_ui.route('/m/registerMditing')
@referrer
@cross_origin()
def registerMditing():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/registerMditing.html', invitationCode=invitation_code)


@web_ui.route('/m/registerTwo')
@referrer
@cross_origin()
def register_mobile_phoneTwo():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/register_phoneTwo.html', invitationCode=invitation_code)

@web_ui.route('/m/register_phoneTwo_resdit')
@referrer
@cross_origin()
def register_phoneTwo_resdit():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/register_phoneTwo_resdit.html', invitationCode=invitation_code)

@web_ui.route('/m/hongbao')
@referrer
@cross_origin()
def register_phoneTwo_hongbao():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/register_phoneTwo_hongbao.html', invitationCode=invitation_code)



@web_ui.route('/m/newYearRegister')
@referrer
@cross_origin()
def newYearRegister():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    loginInfo=get_login_info()
    if(session.get('user_id') is None):
                login_info = {}
                login_info['userType'] = None
                login_info['userName'] = None
                login_info['uniqueId'] = None
                login_info['shortDomainName'] = None
                loginInfo=login_info
    return render_template('web/phone/cornerAI/cornerAIRegister.html', invitationCode=invitation_code, loginInfo=loginInfo)

@web_ui.route('/api/update/telephone_switch/<type>', methods=['POST'])
@referrer
@cross_origin()
def api_update_telephone_switch(type):
    reset_response = requests.post(API_URL + 'accounts/update/telephone_switch/'+type, headers=get_headers())
    if reset_response.status_code == 200:
        resp = jsonify({'message': u'修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'修改失败！'})
        resp.status_code =400
        return resp

@web_ui.route('/api/accounts/register/mobile', methods=['POST'])
@referrer
@cross_origin()
def api_accounts_register_mobile():
    data = json.loads(request.data)
    session['user_type'] = u'DITING_USER'
    register_resp = requests.post(API_URL + 'accounts/register/mobile', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        account = register_resp.json()
        create_session(account)
        resp = jsonify({'message': u'注册登录成功！','uniqueId': account.get("uniqueId")})
        resp.status_code = 200
        return resp
    else:
        message = u'注册失败！请重新注册！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@web_ui.route('/api/accounts/register/mobile/excel', methods=['POST'])
@referrer
@cross_origin()
def api_accounts_register_mobile_excel():
    data = json.loads(request.data)
    session['user_type'] = u'DITING_USER'
    register_resp = requests.post(API_URL + 'accounts/register/mobile/excel', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        account = register_resp.json()
        # create_session(account)
        resp = jsonify({'message': u'注册成功！','uniqueId': account.get("uniqueId")})
        resp.status_code = 200
        return resp
    else:
        message = u'注册失败！请重新注册！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@web_ui.route('/api/accounts/newyearregister/mobile', methods=['POST'])
@referrer
@cross_origin()
def api_accounts_new_year_register_mobile():
    data = json.loads(request.data)
    session['user_type'] = u'DITING_USER'
    register_resp = requests.post(API_URL + 'accounts/register/mobile', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        account = register_resp.json()
        create_session(account)
        resp = jsonify({'message': u'注册登录成功！','uniqueId': account.get("uniqueId")})
        resp.status_code = 200
        return resp
    else:
        message = u'注册失败！请重新注册！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


@web_ui.route('/login')
@referrer
@cross_origin()
def login():
    user_agent = str(request.headers.get('User-Agent')).lower()
    if request.base_url.find("antusheng.cn") >= 0 or request.base_url.find("ditingai.cc")>=0:
        return redirect('/m/login')
    elif re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) and request.base_url.find("ditingai.com") >= 0:
        return redirect('/m/loginMditing')
    else:
        response = Response()
        response.headers['Access-Control-Allow-Origin'] = "*"
        return render_template('web/ditingpc1.0/login.html')

@web_ui.route('/m/login')
@referrer
@cross_origin()
def login_phone():
    return render_template('web/phone/login_phone.html')

@web_ui.route('/m/loginMditing')
@referrer
@cross_origin()
def loginMditing():
    return render_template('web/phone/loginMditing.html')

@web_ui.route('/m/wks_in')
@referrer
@cross_origin()
def wks_in():
    return render_template('web/phone/wks_in.html')

@web_ui.route('/m/cornerAI_login')
@referrer
@cross_origin()
def cornerAI_login():
    return render_template('web/phone/cornerAI/cornerAI_login.html')

@web_ui.route('/m/indexPage')
@referrer
@cross_origin()
def indexPage():
    loginInfo = get_login_info()
    robot = get_robot_info()
    account = get_account_info()
    return render_template('web/phone/cornerAI/indexPage.html',loginInfo=loginInfo, robot=robot,account=account)

@web_ui.route('/m/askModule')
@referrer
@cross_origin()
def askModule():
    account = get_account_info()
    loginInfo = get_login_info()
    return render_template('web/phone/cornerAI/askModule.html',account=account,loginInfo=loginInfo)

@web_ui.route('/m/messModule')
@referrer
@cross_origin()
def messModule():
    return render_template('web/phone/cornerAI/messModule.html')

@web_ui.route('/m/myPage')
@referrer
@cross_origin()
def myPage():
    loginInfo = get_login_info()
    robot = get_robot_info()
    account = get_account_info()
    return render_template('web/phone/cornerAI/myPage.html', loginInfo=loginInfo, robot=robot,account=account)

@web_ui.route('/m/cornerAI_psdReset')
@referrer
@cross_origin()
def cornerAI_psdReset():
    return render_template('web/phone/cornerAI/cornerAI_psdReset.html')

@web_ui.route('/binding_login')
@referrer
@cross_origin()
def binding_login():
    return render_template('web/binding_login.html')


@web_ui.route('/api/login', methods=['POST'])
@referrer
@cross_origin()
def api_login():
    request_data = json.loads(request.data)
    login_response = requests.post(API_URL + 'accounts/login', headers=get_headers(), data=json.dumps(request_data))
    if login_response.status_code == 200:
        account = login_response.json()
        create_session(account)
        resp = jsonify({'headImgUrl': account.get("headImgUrl"),'message': u'登录成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'可爱的小伙伴，您输入的帐户名或密码有误~~~'})
        resp.status_code = 400
        return resp


@web_ui.route('/password-reset')
@referrer
@cross_origin()
def password_back():
    return render_template('web/ditingpc1.0/forget_password.html')

@web_ui.route('/m/password-reset')
@referrer
@cross_origin()
def password_back_phone():
    return render_template('web/phone/forget_pwd_phone.html')


@web_ui.route('/api/password/reset', methods=['POST'])
@referrer
@cross_origin()
def api_password_reset():
    request_data = json.loads(request.data)
    reset_response = requests.post(API_URL + 'accounts/password/reset', headers=get_headers(),
                                   data=json.dumps(request_data))
    if reset_response.status_code == 200:
        resp = jsonify({'message': u'密码找回成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': reset_response.json().get('message')})
        resp.status_code = 400
        return resp


@web_ui.route('/logout')
@referrer
@cross_origin()
def logout():
    session['user_id'] = None
    session['user_name'] = None
    session['user_type'] = None
    return redirect("/login")


@web_ui.route('/agreement')
@referrer
@cross_origin()
def agreement():
    return render_template('web/ditingpc1.0/agreement.html')

@web_ui.route('/m/talk-rank')
@referrer
@cross_origin()
def robot_talk_rank():
    return render_template('web/phone/robot_talk_rank.html')

@web_ui.route('/m/newYearMoney_rank')
@referrer
@cross_origin()
def newYearMoney_rank():
    loginInfo = get_login_info()
    robot = get_robot_info()
    account = get_account_info()
    if(session.get('user_id') is None):
          login_info = {}
          login_info['userType'] = None
          login_info['userName'] = None
          login_info['uniqueId'] = None
          login_info['shortDomainName'] = None
          loginInfo=login_info
    return render_template('web/phone/cornerAI/Money_rank.html',loginInfo=loginInfo, robot=robot,account=account)

@web_ui.route('/m/cornerMatching')
@referrer
@cross_origin()
def cornerMatching():
    loginInfo=get_login_info()
    robot = get_robot_info()
    company = get_company_info(robot.get('companyId'))
    if(session.get('user_id') is None):
        login_info = {}
        login_info['userType'] = None
        login_info['userName'] = None
        login_info['uniqueId'] = None
        login_info['shortDomainName'] = None
        loginInfo=login_info
    return render_template('web/phone/cornerAI/cornerMatching.html',loginInfo=loginInfo,robot=robot,company=company)


@web_ui.route('/m/home')
@referrer
@login_required
@cross_origin()
def phone_home():
    return render_template('web/phone/success_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/index')
@referrer
@cross_origin()
def phone_index():
    robot = get_robot_info()
    return render_template('web/phone/index_phone.html', loginInfo=get_login_info(),robot=robot)

@web_ui.route('/atsIndex')
@referrer
@cross_origin()
def atsIndex():
    robot = get_robot_info()
    return render_template('web/antushengPc1.0/index.html',loginInfo=get_login_info(),robot=robot)

@web_ui.route('/atsSetting')
@referrer
@cross_origin()
def atsSetting():
    robot = get_robot_info()
    return render_template('web/antushengPc1.0/atsSetting.html',loginInfo=get_login_info(),robot=robot)

@web_ui.route('/atsLogin')
@referrer
@cross_origin()
def atsLogin():
    robot = get_robot_info()
    return render_template('web/antushengPc1.0/atsLogin.html',loginInfo=get_login_info(),robot=robot)

@web_ui.route('/atsRegister')
@referrer
@cross_origin()
def atsRegister():
    robot = get_robot_info()
    return render_template('web/antushengPc1.0/atsRegister.html',loginInfo=get_login_info(),robot=robot)

@web_ui.route('/atsForget')
@referrer
@cross_origin()
def atsForget():
    robot = get_robot_info()
    return render_template('web/antushengPc1.0/atsForgetPsd.html',loginInfo=get_login_info(),robot=robot)

@web_ui.route('/ditingCC')
@referrer
@cross_origin()
def ditingCC():
    robot = get_robot_info()
    return render_template('web/ditingCC.html', loginInfo=get_login_info(),robot=robot)

@web_ui.route('/m/')
@referrer
@cross_origin()
def m_index():
    return render_template('web/phone/m_index.html')

@web_ui.route('/m/myStation')
@referrer
@login_required
@cross_origin()
def myStation_phone():
    return render_template('web/phone/myStation_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/logout_phone')
@referrer
@login_required
@cross_origin()
def logout_phone():
    return render_template('web/phone/logout_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/mySelf')
@referrer
@login_required
@cross_origin()
def mySelf_phone():
    return render_template('web/phone/mySelf_phone.html', loginInfo=get_login_info())


@web_ui.route('/m/robotSet')
@referrer
@login_required
@cross_origin()
def phone_robotSet():
    return render_template('web/phone/robotSet_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/knows')
@referrer
@login_required
@cross_origin()
def phone_knows():
    return render_template('web/phone/knows_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/knowledge')
@referrer
@login_required
@cross_origin()
def phone_knowledge():
    return render_template('web/phone/knowledge_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/claim')
@referrer
@cross_origin()
def claim():
    invitation_code = "" if request.args.get('yaoqingma') is None else request.args.get('yaoqingma')
    return render_template('web/phone/robot_no_Receive.html', invitationCode=invitation_code)


@web_ui.route('/about')
@referrer
@cross_origin()
def about():
    return render_template('web/ditingpc1.0/about_diting.html')

@web_ui.route('/photo')
@referrer
@cross_origin()
def photo():
    return render_template('web/PHOTO.html')


@web_ui.route('/contact_us')
@referrer
@cross_origin()
def contact_us():
    return render_template('web/ditingpc1.0/contact_us.html')


@web_ui.route('/cooperative_partner')
@referrer
@cross_origin()
def cooperative_partner():
    return render_template('web/ditingpc1.0/cooperative_partner.html')


@web_ui.route('/home/index')
@referrer
@key_information
@login_required
@cross_origin()
def main_index():
    return render_template('web/ditingpc1.0/success_index.html', loginInfo=get_login_info())

@web_ui.route('/aboutTransaction')
@referrer
@login_required
@cross_origin()
def aboutTransaction():
    return render_template('web/aboutTransaction.html', loginInfo=get_login_info())

@web_ui.route('/api/dictionaries/search-all', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_dictionaries_searchall():
    dictionaries = []
    tag = "" if request.args.get('tag') is None else request.args.get('tag')
    select_response = requests.get(API_URL + 'dictionaries/search-all?tag' + str(tag), headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))


@web_ui.route('/api/company/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_save():
    request_data = json.loads(request.data)

    account = get_account_info()
    if account.get("companyId") is not None:
        request_data["id"] = account.get("companyId")

    save_response = requests.post(API_URL + 'companies/save', headers=get_headers(), data=json.dumps(request_data))
    if save_response.status_code == 200:
        robot = get_robot_info()
        url = ''
        if robot.get('name') is None or robot.get('shortDomainName') is None:
            url = '/robot-setting'
        resp = jsonify({'message': u'企业信息保存成功！', 'url': url})
        resp.status_code = 200
        return resp
    else:
        message = u'企业信息保存失败！' if save_response.json().get('message') is None else save_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


@web_ui.route('/api/get-company-info', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_get_company_info():
    company = {}
    account = get_account_info()
    company_id = account.get("companyId")
    if company_id is not None:
        company_response = requests.get(API_URL + 'companies/' + str(company_id), headers=get_headers())
        if company_response.status_code == 200:
            company = company_response.json()
    return Response(json.dumps(company))


@web_ui.route('/api/company/search-for-keyword', methods=['GET'])
@referrer
@cross_origin()
def api_get_company_search_keyword():
    company = {}
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    company_response = requests.get(API_URL + 'companies/searchforkeyword?keyword=' + keyword, headers=get_headers())
    if company_response.status_code == 200:
        company = company_response.json()

    return Response(json.dumps(company))

@web_ui.route('/api/get-account-info', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_get_account_info():
    account = get_account_info()
    return Response(json.dumps(account))


@web_ui.route('/api/account/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_account_save():
    request_data = json.loads(request.data)

    account = get_account_info()
    account["realName"] = request_data.get("realName")
    account["email"] = request_data.get("email")

    save_response = requests.put(API_URL + 'accounts/update', headers=get_headers(), data=json.dumps(account))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'账号信息编辑成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'账号信息编辑失败！'})
        resp.status_code = 400
        return resp


@web_ui.route('/weixin-change')
@referrer
@login_required
@cross_origin()
def weixin_change():
    return render_template('web/weixin-change.html', loginInfo=get_login_info())

@web_ui.route('/m/reset_pwd_phone')
@referrer
@login_required
@cross_origin()
def reset_pwd_phone():
    return render_template('web/phone/reset_pwd_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/user_admin_phone')
@referrer
@login_required
@cross_origin()
def user_admin_phone():
    return render_template('web/phone/user_admin_phone.html', loginInfo=get_login_info())

@web_ui.route('/m/user_message_phone')
@referrer
@login_required
@cross_origin()
def user_message_phone():
    return render_template('web/phone/user_message_phone.html', loginInfo=get_login_info())


@web_ui.route('/api/password/change', methods=['POST'])
@referrer
@login_required
@cross_origin()
def api_password_change():
    request_data = json.loads(request.data)
    reset_response = requests.post(API_URL + 'accounts/password/change', headers=get_headers(),
                                   data=json.dumps(request_data))
    if reset_response.status_code == 200:
        resp = jsonify({'message': u'密码修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': reset_response.json().get('message')})
        resp.status_code = 400
        return resp


@web_ui.route('/channel')
@referrer
@login_required
@key_information
@cross_origin()
def channel():
    return render_template('web/access_download.html', loginInfo=get_login_info(), showFlag='channel')

@web_ui.route('/robot_talkAboutRental')
@referrer
@login_required
@key_information
@cross_origin()
def robot_talkAboutRental():
    robot = get_robot_info()
    return render_template('web/ditingpc1.0/robot_talkAboutRental.html', loginInfo=get_login_info(), robot=robot)

@web_ui.route('/m/newYearRobot/<unique_id>')
@referrer
# @login_required
# @key_information
@cross_origin()
def newYearRobot(unique_id):
    loginInfo = get_login_info()
    account = get_account_info()
    # robot = get_robot_info()
    # company = get_company_info(robot.get('companyId'))
    # account = get_account_by_company_id(robot.get('companyId'))
    if session.get('user_id') is not None:
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')
    robot = get_company_robot_info(unique_id)
    company = get_company_info(robot.get('companyId'))
    account = get_account_by_company_id(robot.get('companyId'))
    if(session.get('user_id') is None):
            login_info = {}
            login_info['userType'] = None
            login_info['userName'] = None
            login_info['uniqueId'] = None
            login_info['shortDomainName'] = None
            loginInfo=login_info
    return render_template('web/phone/cornerAI/cornerAIRobot.html', loginInfo=loginInfo, robot=robot,
                               company=company,account=account, claimEnable=account.get("claimEnable"))

@web_ui.route('/robot-company/<unique_id>')
@referrer
@cross_origin()
def robot_company(unique_id):
    if session.get('user_id') is not None:
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')

        robot = get_robot_info()
        if robot.get('name') is None or robot.get('shortDomainName') is None:
            return redirect('/robot-setting')
    robot = get_company_robot_info(unique_id)
    company = get_company_info(robot.get('companyId'))
    account = get_account_by_company_id(robot.get('companyId'))
    user_agent = str(request.headers.get('User-Agent')).lower()
    if re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) :
        return render_template('web/phone/robot_talk_phone.html', loginInfo=get_login_info(),robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))
    else:
        return render_template('web/ditingpc1.0/robot_talk.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))


@web_ui.route('/company/robot/<domainName>')
@referrer
@cross_origin()
def company_robot(domainName):
    if session.get('user_id') is not None:
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')

        robot = get_robot_info()
        if robot.get('name') is None or robot.get('shortDomainName') is None:
            return redirect('/robot-setting')
    robot = get_company_robot_info_by_name(domainName)
    company = get_company_info(robot.get('companyId'))
    account = get_account_by_company_id(robot.get('companyId'))

    user_agent = str(request.headers.get('User-Agent')).lower()
    if re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) :
        return render_template('web/phone/robot_talk_phone.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))
    else:
        return render_template('web/ditingpc1.0/robot_talk.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))


@web_ui.route('/co-bot/<domainName>')
@referrer
@cross_origin()
def co_bot(domainName):
    if session.get('user_id') is not None:
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')

        robot = get_robot_info()
        if robot.get('name') is None or robot.get('shortDomainName') is None:
            return redirect('/robot-setting')
    robot = get_company_robot_info_by_name(domainName)
    company = get_company_info(robot.get('companyId'))
    account = get_account_by_company_id(robot.get('companyId'))

    user_agent = str(request.headers.get('User-Agent')).lower()
    if re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) :
        return render_template('web/phone/robot_talk_phone.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))
    else:
        return render_template('web/ditingpc1.0/robot_talk.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))


@web_ui.route('/r/<domainName>')
@referrer
@cross_origin()
def r(domainName):
    if session.get('user_id') is not None:
        company_id = get_account_info().get("companyId")
        if company_id is None:
            return redirect('/company-info')
        else:
            company = get_company_info(company_id)
            if company.get('name') is None:
                return redirect('/company-info')

        robot = get_robot_info()
        if robot.get('name') is None or robot.get('shortDomainName') is None:
            return redirect('/robot-setting')
    robot = get_company_robot_info_by_name(domainName)
    company = get_company_info(robot.get('companyId'))
    account = get_account_by_company_id(robot.get('companyId'))

    user_agent = str(request.headers.get('User-Agent')).lower()
    if re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) and request.base_url.find("localhost") >= 0 :
        return render_template('web/phone/robot_talk_phoneRental.html', loginInfo=get_login_info(), robot=robot,
                           company=company, claimEnable=account.get("claimEnable"))
    elif request.base_url.find("localhost") >= 0 :
        return render_template('web/ditingpc1.0/robot_talkAboutRental.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))
    elif re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) :
        return render_template('web/phone/robot_talk_phone.html', loginInfo=get_login_info(), robot=robot,
                       company=company, claimEnable=account.get("claimEnable"))
    else:
        return render_template('web/ditingpc1.0/robot_talk.html', loginInfo=get_login_info(), robot=robot,
                               company=company, claimEnable=account.get("claimEnable"))



@web_ui.route('/api/robot/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_robot_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'robots/save', headers=get_headers(), data=json.dumps(request_data))
    if save_response.status_code == 200:
        robot = get_robot_info()
        session['unique_id'] = robot.get('uniqueId')
        session['domain_name'] = robot.get('shortDomainName')
        resp = jsonify({'message': u'机器人信息保存成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'机器人信息保存失败！' if save_response.json().get('message') is None else save_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


#图片上传
@web_ui.route('/upload/<element>', methods=['POST'])
@cross_origin()
@referrer
def upload_img(element):
    error = ''
    url = ''
    # 要上传的空间
    print request
    if request.method == 'POST':
        print request.files
        fileobj = request.files['file']
        print fileobj.filename
        fname, fext = os.path.splitext(fileobj.filename)
        rnd_name = '%s%s' % (qiniu_image.random_str(22), fext)
        static_path = app.static_folder
        filepath = os.path.join(static_path.replace('views/', ''), 'upload', rnd_name)
        dirname = os.path.dirname(filepath)
        if not os.path.exists(dirname):
            try:
                os.makedirs(dirname)
            except:
                error = 'ERROR_CREATE_DIR'
        elif not os.access(dirname, os.W_OK):
            error = 'ERROR_DIR_NOT_WRITEABLE'

        if not error:
            fileobj.save(filepath)
            #上传七牛
            qiniu_data = {}
            qiniu_data["upload_url"]=filepath;
            qiniu_data["rnd_name"]=rnd_name;
            qiniu_response = requests.post(
                API_URL + 'qiniu/upload-image' ,data = json.dumps(qiniu_data),
                headers=get_headers())

            # url = qiniu_image.upload_file(filepath,rnd_name)
            # q.upload_token('diting-picture',rnd_name,3600)
            # if url != None:
            #     os.remove(filepath)
            if qiniu_response.status_code==200:
                qiniu_json = qiniu_response.json()
                url=qiniu_json.get("img_url")
                os.remove(filepath)
    else:
        error = 'post error'
    resp = jsonify({'url': url, 'error': error})
    resp.status_code = 200
    resp.headers["Content-Type"] = "text/html"
    return resp
    # return Response(json.dumps(qiniu_response.json()))


@web_ui.route('/api/get-robot-info', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_get_robot_info():
    robot = {}
    robot_response = requests.get(API_URL + 'robots/user/' + str(session.get("user_id")), headers=get_headers())
    if robot_response.status_code == 200:
        robot = robot_response.json()
    return Response(json.dumps(robot))


@web_ui.route('/api/claim-robots', methods=['GET'])
@referrer
@cross_origin()
def api_get_claim_robots():
    robots = {}
    robot_response = requests.get(API_URL + 'robots/claim-robots', headers=get_headers())
    if robot_response.status_code == 200:
        robots = robot_response.json()
    return Response(json.dumps(robots))

@web_ui.route('/exports')
@referrer
@login_required
@cross_origin()
def exports():
    return render_template('web/exports.html', loginInfo=get_login_info(), showFlag='setting')

@web_ui.route('/api/captchas/mobile/<mobile>', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_captchas_mobile(mobile):
    # check mobile
    mobile_resp = requests.get(API_URL + 'accounts/check-mobile/' + mobile,
                               headers=get_headers(), data=request.data, stream=True)

    if (mobile_resp.status_code == 200):
        resp = jsonify({'message': u'手机号【' + mobile + '】不存在!'})
        resp.status_code = 400
        return resp

    # get verifyCode
    captcha_resp = requests.get(API_URL + 'captchas/mobile/' + mobile, headers=get_headers(),
                                data=request.data, stream=True)

    if (captcha_resp.status_code == 200):
        resp = jsonify({'message': u'验证码获取成功!'})
        resp.status_code = 200
        return resp
    else:
        message = u'验证码获取失败！' if captcha_resp.json().get('message') is None else captcha_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


@web_ui.route('/api/reset/captchas/mobile/<mobile>', methods=['GET'])
@referrer
@cross_origin()
def api_reset_captchas_mobile(mobile):
    # check mobile
    mobile_resp = requests.get(API_URL + 'accounts/check-mobile/' + mobile,
                               headers=get_headers(), data=request.data, stream=True)

    if (mobile_resp.status_code == 200):
        resp = jsonify({'message': u'手机号【' + mobile + '】不存在!'})
        resp.status_code = 400
        return resp

    # get verifyCode
    captcha_resp = requests.get(API_URL + 'captchas/mobile/' + mobile, headers=get_headers(),
                                data=request.data, stream=True)

    if (captcha_resp.status_code == 200):
        resp = jsonify({'message': u'验证码获取成功!'})
        resp.status_code = 200
        return resp
    else:
        message = u'验证码获取失败！' if captcha_resp.json().get('message') is None else captcha_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


@web_ui.route('/api/register/captchas/mobile/<mobile>', methods=['GET'])
@referrer
@cross_origin()
def api_register_captchas_mobile(mobile):
    # check mobile
    mobile_resp = requests.get(API_URL + 'accounts/check-mobile/' + mobile,
                               headers=get_headers(), data=request.data, stream=True)

    if (mobile_resp.status_code != 200):
        resp = jsonify({'message': u'手机号【' + mobile + '】已存在!'})
        resp.status_code = 400
        return resp

    # get verifyCode
    captcha_resp = requests.get(API_URL + 'captchas/mobile/' + mobile, headers=get_headers(),
                                data=request.data, stream=True)

    if (captcha_resp.status_code == 200):
        resp = jsonify({'message': u'验证码获取成功!'})
        resp.status_code = 200
        return resp
    else:
        message = u'验证码获取失败！' if captcha_resp.json().get('message') is None else captcha_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


@web_ui.route('/api/bind-mobile', methods=['POST'])
@referrer
@cross_origin()
def api_account_bind():
    request_data = json.loads(request.data)
    bind_response = requests.post(API_URL + 'accounts/bind-mobile', headers=get_headers(),data=json.dumps(request_data))
    if bind_response.status_code == 200:
        request_data = bind_response.json()
        chat_data = {}
        chat_data["oldUsername"] = request_data.get('oldMobile')
        chat_data["newUsername"] = request_data.get('mobile')
        requests.post(API_URL + 'chat/record/update_username', headers=get_headers(), data=json.dumps(chat_data))
        resp = jsonify({'message': u'认领平行人成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'认领平行人失败！' if bind_response.json().get('message') is None else bind_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


def create_session(account):
    session['user_id'] = account.get('id')
    session['user_type'] = u'DITING_USER'
    session['user_name'] = account.get('userName')
    session['unique_id'] = get_robot_info().get('uniqueId')
    session['domain_name'] = get_robot_info().get('shortDomainName')
    session['robot_val']=account.get('robotVal')
    session['balance'] =account.get('balance')
    session['headPortrait']=account.get('headImgUrl')


def get_login_info():
    loginInfo = {}
    loginInfo['userType'] = session.get('user_type')
    loginInfo['userName'] = session.get('user_name')
    loginInfo['uniqueId'] = session.get('unique_id')
    loginInfo['shortDomainName'] = session.get('domain_name')
    loginInfo['robotVal'] =session.get('robot_val')
    loginInfo['balance'] =session.get('balance')
    loginInfo['headPortrait'] =session.get('headPortrait')
    return loginInfo


def get_account_info():
    account = {}
    account_response = requests.get(API_URL + 'accounts/' + str(session.get('user_id')), headers=get_headers())
    if account_response.status_code == 200:
        account = account_response.json()
    return account


def get_robot_info():
    robot = {}
    robot_response = requests.get(API_URL + 'robots/user/' + str(session.get('user_id')), headers=get_headers())
    if robot_response.status_code == 200:
        robot = robot_response.json()
    return robot


def get_company_robot_info(unique_id):
    robot = {}
    robot_response = requests.get(API_URL + 'robots/unique_id/' + unique_id, headers=get_headers())
    if robot_response.status_code == 200:
        robot = robot_response.json()
    return robot


def get_account_by_company_id(company_id):
    account = {}
    account_response = requests.get(API_URL + 'accounts/company/' + str(company_id), headers=get_headers())
    if account_response.status_code == 200:
        account = account_response.json()
    return account


def get_company_robot_info_by_name(domainName):
    robot = {}
    robot_response = requests.get(API_URL + 'robots/domain_name/' + str(domainName), headers=get_headers())
    if robot_response.status_code == 200:
        robot = robot_response.json()
    return robot


def get_company_info(company_id):
    company = {}
    company_response = requests.get(API_URL + 'companies/' + str(company_id), headers=get_headers())
    if company_response.status_code == 200:
        company = company_response.json()
    return company


def utc2local(utc_st):
    now_stamp = time.time()
    local_time = datetime.datetime.fromtimestamp(now_stamp)
    utc_time = datetime.datetime.utcfromtimestamp(now_stamp)
    offset = local_time - utc_time
    local_st = utc_st + offset
    return local_st


def utcformate(utc_time):
    UTC_FORMAT = '%Y-%m-%dT%H:%M:%SZ'
    return datetime.datetime.strptime(utc_time, UTC_FORMAT)


@web_ui.route('/all_questions_rank')
# @referrer
# @cross_origin()
def all_questions_rank():
    return render_template('web/all_questions_rank.html')


@web_ui.route('/yesterday_questions')
# @referrer
# @cross_origin()
def yesterday_questions():
    return render_template('web/yesterday_questions.html')

@web_ui.route('/activity_questions')
# @referrer
# @cross_origin()
def activity_questions():
    return render_template('web/activity_questions.html')

@web_ui.route('/activity_vote')
@referrer
@login_required
@cross_origin()
def activity_vote():
    return render_template('web/activity_vote.html', loginInfo=get_login_info())

@web_ui.route('/activity_voteRule')
@referrer
@login_required
@cross_origin()
def activity_voteRule():
    return render_template('web/activity_voteRule.html')

@web_ui.route('/api/chat/info', methods=['POST'])
@referrer  # 请求来源
@cross_origin()  # 转发
def chat_base():
    request_data = json.loads(request.data)
    chat_resp = requests.post(API_URL + 'chats/questions/answers', headers=get_headers(), data=json.dumps(request_data))
    chat = chat_resp.json()
    return Response(json.dumps(chat))

@web_ui.route('/angel/chat/info', methods=['POST'])
@referrer  # 请求来源
@cross_origin()  # 转发
def angel_chat_base():
    request_data = json.loads(request.data)
    chat_resp = requests.post(API_URL + 'chats/angel/questions/answers', headers=get_headers(),
                              data=json.dumps(request_data))
    # chat = chat_resp.json()
    # return Response(json.dumps(chat))
    if chat_resp.status_code==200:
        chat = chat_resp.json()
        resp = jsonify({'message':'success','answer': chat.get("answer"),'img_url':chat.get("img_url"),'skip_url':chat.get('skip_url'),'welcome':chat.get('welcome'),'username':chat.get("username"),'robotName':chat.get('robotName'),'companyName':chat.get('companyName'),'headImgUrl':chat.get('headImgUrl'),'profile':chat.get('profile'),'action':{'actionOption': chat.get("actionOption"),'question':chat.get("question"),'available':chat.get("available")}})
        return resp
    else:
        resp = jsonify({'message': 'failed', 'answer': 'failed', 'actionOption': 'failed'})
        return resp

@web_ui.route('/remote/chat/info', methods=['POST'])
@referrer  # 请求来源
@cross_origin()  # 转发
def remote_chat_base():
    # print request.data
    request_data = json.loads(request.data)
    chat_resp = requests.post(API_URL + 'chats/remote/questions/answers', headers=get_headers(),
                              data=json.dumps(request_data))
    # chat = chat_resp.json()
    # return Response(json.dumps(chat))
    if chat_resp.status_code==200:
        chat = chat_resp.json()
        resp = jsonify({'message':'success','answer': chat.get("answer"),'img_url':chat.get("img_url"),'skip_url':chat.get('skip_url'),'welcome':chat.get('welcome'),'username':chat.get("username"),'robotName':chat.get('robotName'),'companyName':chat.get('companyName'),'headImgUrl':chat.get('headImgUrl'),'profile':chat.get('profile'),'action':{'actionOption': chat.get("actionOption"),'question':chat.get("question"),'available':chat.get("available")}})
        return resp
    else:
        resp = jsonify({'message': 'failed', 'answer': 'failed', 'actionOption': 'failed'})
        return resp



# 知识库分页查询
@web_ui.route('/api/company/knowledge/search-page', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_company_knowledge_searchpage():
    dictionaries = []
    type = "0" if request.args.get('type') is None else request.args.get('type')
    page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    company_id = "" if request.args.get('companyId') is None else request.args.get('companyId')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    query_state = "" if request.args.get('queryState') is None else request.args.get('queryState')
    query_criteria = "" if request.args.get('queryCriteria') is None else request.args.get('queryCriteria')
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    select_response = requests.get(
        API_URL + 'knowledge/search-page?pageNo=' + page_no + '&companyId=' + company_id + '&keywords=' + keywords + '&queryState=' + query_state + '&queryCriteria=' + query_criteria + '&starttime=' + starttime + '&endtime=' + endtime + '&type=' + type,
        headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))

# 手机端无效问题查询
@web_ui.route('/api/invalid-question/search', methods=['GET'])
@referrer
@cross_origin()
def api_invalid_question_searchmobile():
    dictionaries = []
    question = "" if request.args.get('question') is None else request.args.get('question')
    uuid = "" if request.args.get('uuid') is None else request.args.get('uuid')
    select_response = requests.get(API_URL + 'chat/record/invalid-question/search-mobile?question=' + question + '&uuid=' + uuid , headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))


# 新增知识
@web_ui.route('/api/knowledge/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_knowledge_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/create', headers=get_headers(), data=json.dumps(request_data))
    return Response(json.dumps(save_response.json()))

# 远程-新增知识
@web_ui.route('/remote/knowledge/save', methods=['POST'])
@referrer
@cross_origin()
def remote_company_knowledge_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/create', headers=get_headers(), data=json.dumps(request_data))
    return Response(json.dumps(save_response.json()))

# 远程-新增知识
@web_ui.route('/temp/knowledge/save', methods=['POST'])
@referrer
@cross_origin()
def temp_company_knowledge_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/temp/create', headers=get_headers(), data=json.dumps(request_data))
    return Response(json.dumps(save_response.json()))

# get scene
@web_ui.route('/api/knowledge/scene', methods=['POST'])
@referrer
@cross_origin()
def api_get_scene():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/scene', headers=get_headers(), data=json.dumps(request_data))
    # print json.dumps(save_response.json())
    return Response(json.dumps(save_response.json()))


# 根据id删除知识
@web_ui.route('/api/knowledge/delete/<knowledgeId>', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_knowledge_delete(knowledgeId):
    save_response = requests.put(API_URL + 'knowledge/delete/' + knowledgeId, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp


# 根据id批量删除知识
@web_ui.route('/api/knowledge/batchdelete', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_knowledge_batch_delete():
    request_data = json.loads(request.data)
    save_response = requests.put(API_URL + 'knowledge/batchdelete', headers=get_headers(),
                                 data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp


# 根据id查询知识信息
@web_ui.route('/api/knowledge/find/<knowledgeId>', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_get_knowledge_info(knowledgeId):
    knowledge = {}
    knowledge_response = requests.get(API_URL + 'knowledge/' + knowledgeId, headers=get_headers())
    if knowledge_response.status_code == 200:
        knowledge = knowledge_response.json()
        response = Response(json.dumps(knowledge))
        # response.headers['Access-Control-Allow-Origin'] = "*"
        # response.headers['Access-Control-Allow-Credentials'] = "true"
        # response.headers['Access-Control-Expose-Headers'] = "FooBar"
        print response.headers
    return response


@web_ui.route('/api/find/base_info', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_get_base_info():
    base_info = {}
    knowledge_response = requests.get(API_URL + 'knowledge/company/baseinfo', headers=get_headers())
    if knowledge_response.status_code == 200:
        base_info = knowledge_response.json()
    return Response(json.dumps(base_info))

# 根据id删除知识
@web_ui.route('/api/recruit/delete/<id>', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_recruit_delete(id):
    delete_response = requests.get(API_URL + 'recruit/delete/' + id, headers=get_headers())
    if delete_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp

@web_ui.route('/api/recruit/create', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_recruit_info_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'recruit/create', headers=get_headers(),
                                  data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！' if save_response.json().get('message') is None else save_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@web_ui.route('/api/recruit/update', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_recruit_info_update():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'recruit/update', headers=get_headers(),
                                  data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'修改成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'修改失败！' if save_response.json().get('message') is None else save_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp
@web_ui.route('/api/knowledge/company/base_knowledge_info', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_base_knowledge_info_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/company/baseknowledgeinfo', headers=get_headers(),
                                  data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'成功生成基础知识库！'})
        resp.status_code = 200
        return resp
    else:
        message = u'基础库生成失败！' if save_response.json().get('message') is None else save_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


# 知识修改
@web_ui.route('/api/knowledge/update', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_knowledge_update():
    request_data = json.loads(request.data)

    update_response = requests.put(API_URL + 'knowledge/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'知识修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'知识修改失败！'})
        resp.status_code = 400
        return resp


# 聊天记录分组查询
@web_ui.route('/api/company/chatlog/search-group', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_company_chatlog_searchgroup():
    dictionaries = []
    pageNo = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    select_response = requests.get(
        API_URL + 'chat/record/group/search-page?pageNo=' + pageNo,
        headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))


# 聊天记录查询
@web_ui.route('/api/company/chatlog/searchpage', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_chatlog_searchpage():
    chatLogs = {}
    pageNo = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    uuid = "" if request.args.get('uuid') is None else request.args.get('uuid')
    select_chatlog_response = requests.get(API_URL + 'chat/record/search-page?pageNo=' + pageNo + '&uuid=' + uuid,
                                           headers=get_headers())
    if select_chatlog_response.status_code == 200:
        chatLogs = select_chatlog_response.json()
    return Response(json.dumps(chatLogs))


# 无效问题记录查询
@web_ui.route('/api/company/invalidquestion/searchpage', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_company_invalidquestion_searchpage():
    invalids = {}
    pageNo = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    select_invalidquestions_response = requests.get(
        API_URL + 'chat/record/invalid-question/search-page?pageNo=' + pageNo + '&keywords=' + keywords + '&starttime=' + starttime + '&endtime=' + endtime,
        headers=get_headers())
    if select_invalidquestions_response.status_code == 200:
        invalids = select_invalidquestions_response.json()
    return Response(json.dumps(invalids))


# 总问答排行
@web_ui.route('/api/company/all/questions_rank', methods=['GET'])
@referrer
@cross_origin()
def api_company_all_ranking():
    all_questions_rank = {}
    all_questions_rank_response = requests.get(
        API_URL + 'chat/record/all/ranking',
        headers=get_headers())
    if all_questions_rank_response.status_code == 200:
        all_questions_rank = all_questions_rank_response.json()
        response = Response(json.dumps(all_questions_rank))
        # response.headers['Access-Control-Allow-Origin'] = '*'
        # response.headers['Access-Control-Allow-Methods'] = 'GET, POST, PUT, DELETE, OPTIONS'
        # response.headers['Access-Control-Allow-Headers'] = 'x-requested-with'
        # response.headers['Access-Control-Max-Age'] = '1800'
        print response.headers
    return response

@web_ui.route('/api/company/top/fifty/questions_rank', methods=['GET'])
@referrer
@cross_origin()
def api_company_top_fifty_ranking():
    all_questions_rank = {}
    all_questions_rank_response = requests.get(
        API_URL + 'chat/record/top/fifty/ranking',
        headers=get_headers())
    if all_questions_rank_response.status_code == 200:
        all_questions_rank = all_questions_rank_response.json()
    return Response(json.dumps(all_questions_rank))


# 昨日问答排行1
@web_ui.route('/api/company/yesterday/questions_rank', methods=['GET'])
@referrer
@cross_origin()
def api_company_yesterday_ranking():
    all_questions_rank = {}
    all_questions_rank_response = requests.get(
        API_URL + 'chat/record/all/yesterday-ranking',
        headers=get_headers())
    if all_questions_rank_response.status_code == 200:
        all_questions_rank = all_questions_rank_response.json()
    return Response(json.dumps(all_questions_rank))


# 昨日问答排行2
@web_ui.route('/robot-company/api/company/yesterday/questions_rank', methods=['GET'])
@referrer
@cross_origin()
def api_robot_company_yesterday_ranking():
    all_questions_rank = {}
    all_questions_rank_response = requests.get(
        API_URL + 'chat/record/all/yesterday-ranking',
        headers=get_headers())
    if all_questions_rank_response.status_code == 200:
        all_questions_rank = all_questions_rank_response.json()
    return Response(json.dumps(all_questions_rank))

# update chatlog
@web_ui.route('/api/chatlog/update_username', methods=['POST'])
@referrer
@cross_origin()
def api_chatlog_update_username():
    data = json.loads(request.data)
    update_invalidquestions_response = requests.post(API_URL + 'chat/record/update_username',
                                                    headers=get_headers(), data=json.dumps(data))
    if update_invalidquestions_response.status_code == 200:
        resp = jsonify({'message': u'编辑成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'编辑失败！'})
        resp.status_code = 400
        return resp


# 无效问题记录编辑
@web_ui.route('/api/company/invalidquestion/update', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_invalidquestion_update():
    data = json.loads(request.data)
    update_invalidquestions_response = requests.put(API_URL + 'chat/record/invalid-question/update',
                                                    headers=get_headers(), data=json.dumps(data))
    if update_invalidquestions_response.status_code == 200:
        resp = jsonify({'message': u'无效问题编辑成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'无效问题编辑失败！'})
        resp.status_code = 400
        return resp


# 无效问题批量删除
@web_ui.route('/api/company/invalidquestion/batchdelete', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_company_invalidquestion_batchdelete():
    id_str = "" if request.args.get('id_str') is None else request.args.get('id_str')
    ids = id_str.replace("|", "&")
    batchdelete_invalidquestions_response = requests.get(
        API_URL + 'chat/record/invalid-question/batchdelete?' + ids, headers=get_headers())
    if batchdelete_invalidquestions_response.status_code == 200:
        resp = jsonify({'message': u'无效问题批量删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'无效问题批量删除失败！'})
        resp.status_code = 400
        return resp


@web_ui.route('/upload', methods=['GET', 'POST'])
@referrer
def upload_file():
    if request.method == 'POST':
        f = request.files['files[]']
        filename = f.filename
        minetype = f.content_type
        static_path = app.static_folder
        file_path = str(os.path.join(static_path.replace('views/', ''), 'upload', ''))
        dir_name = os.path.dirname(file_path)
        if not os.path.exists(dir_name):
            try:
                os.makedirs(dir_name)
            except:
                print 'ERROR_CREATE_DIR'
        elif not os.access(dir_name, os.W_OK):
            print 'ERROR_DIR_NOT_WRITEABLE'

        f.save(file_path + filename)
        upload_path = file_path + filename
        batchdelete_invalidquestions_response = requests.get(API_URL + 'knowledge/upload?upload_path=' + upload_path,
                                                             headers=get_headers())
    return json.dumps(batchdelete_invalidquestions_response.content)

@web_ui.route('/app/upload', methods=['GET', 'POST'])
@referrer
def app_upload_file():
    if request.method == 'POST':
        f = request.files['files[]']
        filename = f.filename
        minetype = f.content_type
        static_path = app.static_folder
        file_path = str(os.path.join(static_path.replace('views/', ''), 'app', ''))
        dir_name = os.path.dirname(file_path)
        if not os.path.exists(dir_name):
            try:
                os.makedirs(dir_name)
            except:
                print 'ERROR_CREATE_DIR'
        elif not os.access(dir_name, os.W_OK):
            print 'ERROR_DIR_NOT_WRITEABLE'

        f.save(file_path + filename)
        upload_path = file_path + filename
    resp = jsonify({'message': u'上传成功！'})
    resp.status_code = 200
    return resp


# 模板文件下载
@web_ui.route('/api/template/download', methods=['GET', 'POST'])
@referrer
def down_file():
    static_path = app.static_folder
    file_path = static_path + "/download/questionExcel.xls"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response


# 网站客服说明文档下载
@web_ui.route('/api/web_document/download', methods=['GET', 'POST'])
@referrer
def down_web_documents():
    static_path = app.static_folder
    file_path = static_path + "/download/userguide.pdf"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response


# 中文切词说明文档下载
@web_ui.route('/api/word_segmentation/download', methods=['GET', 'POST'])
@referrer
def down_word_segmentation_document():
    static_path = app.static_folder
    file_path = static_path + "/download/qieci.docx"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response


# 机器人API说明文档下载
@web_ui.route('/api/website_customer_service/download', methods=['GET', 'POST'])
@referrer
def down_website_customer_service_document():
    static_path = app.static_folder
    file_path = static_path + "/download/websiteCustomerService.docx"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response


# 机器人操作指南文档下载
@web_ui.route('/api/robot_operation_guide/download', methods=['GET', 'POST'])
@referrer
def down_robot_operation_guide_document():
    static_path = app.static_folder
    file_path = static_path + "/download/robotOperationGuide.pdf"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response

# 谛听语义
@web_ui.route('/api/dtrobot_document/download', methods=['GET', 'POST'])
@referrer
def down_dtrobot_documents():
    static_path = app.static_folder
    file_path = static_path + "/download/dtrobotSemantic.apk"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response

# 谛听语义APP操作指南
@web_ui.route('/api/dtrobot_sementic_app_operationGuide/download', methods=['GET', 'POST'])
@referrer
def down_sementic_app_documents():
    static_path = app.static_folder
    file_path = static_path + "/download/dtrobotSementicAppOperationGuide.pdf"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response

@web_ui.route('/MP_verify_1tWj6M9c1CghqPAD.txt', methods=['GET', 'POST'])
@referrer
def MP_verify_1tWj6M9c1CghqPAD():
    static_path = app.static_folder
    file_path = static_path + "/download/MP_verify_1tWj6M9c1CghqPAD.txt"
    company_names = [line.decode('utf-8').strip() for line in open(file_path)]
    resp = company_names[0]
    return resp

# 问答知识库建设说明8月版
@web_ui.route('/api/knowledge_base_construction/download', methods=['GET', 'POST'])
@referrer
def down_knowledge_base_construction_document():
    static_path = app.static_folder
    file_path = static_path + "/download/knowledgeBaseConstruction.pdf"
    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    return response


# 企业知识库批量导出
@web_ui.route('/api/company/exportknowledge', methods=['GET'])
@referrer
def down_company_knowledge():
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    type = "0" if request.args.get('type') is None else request.args.get('type')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    query_state = "" if request.args.get('queryState') is None else request.args.get('queryState')
    knowledge_responses = requests.get(
        API_URL + 'knowledge/exportknowledges?starttime=' + starttime + '&endtime=' + endtime+'&type='+type+'&queryState='+query_state+'&keywords='+keywords, headers=get_headers())
    file_path = generate_excel(knowledge_responses)

    pos = file_path.rfind("/")
    name = file_path[pos + 1:]
    raw_bytes = ""
    with open(file_path, 'rb') as r:
        for line in r:
            raw_bytes = raw_bytes + line
    response = make_response(raw_bytes)
    response.headers['Content-Type'] = "application/octet-stream"
    response.headers['Content-Disposition'] = "inline; filename=" + name
    os.remove(file_path)
    return response


# 用户反馈
@web_ui.route('/api/message/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_company_message_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'message/create', headers=get_headers(), data=json.dumps(request_data))
    return Response(json.dumps(save_response.json()))


def generate_excel(knowledges):
    now = int(time.mktime(datetime.datetime.now().timetuple()) * 1000)
    i = 0
    static_path = app.static_folder
    w = Workbook()  # 创建一个工作簿
    ws = w.add_sheet('export_knowledge')  # 创建一个工作表
    ws.write(0, 0, 'question')  # 在1行1列写入bit
    ws.write(0, 1, 'answer')  # 在1行2列写入huang
    ws.write(0, 2, 'scene')  # 在2行1列写入xuan
    knowledgeList = knowledges.json()
    filename = static_path + '/upload/questionAnswer' + str(now) + '.xls'
    for knowledge in knowledgeList:
        i = i + 1
        ws.write(i, 0, knowledge.get('question'))
        ws.write(i, 1, knowledge.get('answer'))
        ws.write(i, 2, knowledge.get('scene'))
    w.save(filename)  # 保存
    return filename


# save customersynonym
@web_ui.route('/api/customersynonym/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_customersynonym_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'customersynonym/create', headers=get_headers(),
                                  data=json.dumps(request_data))

    if save_response.status_code == 200:
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        # print resp
        return resp
    else:
        resp = jsonify({'message': u'添加失败！'})
        resp.status_code = 400
        return resp


# customersynonym search for page
@web_ui.route('/api/customersynonym/search-page', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_customersynonym_searchpage():
    dictionaries = []
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    select_response = requests.get(
        API_URL + 'customersynonym/search-page?pageNo=' + page_no + '&keywords=' + keywords,
        headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))


# update customersynonym
@web_ui.route('/api/customersynonym/update', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_customersynonym_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'customersynonym/update', headers=get_headers(),
                                   data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'修改失败！'})
        resp.status_code = 400
        return resp


# delete customersynonym by id
@web_ui.route('/api/customersynonym/delete/<customersynonymId>', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_customersynonym_delete(customersynonymId):
    delete_response = requests.post(API_URL + 'customersynonym/delete/' + customersynonymId, headers=get_headers())
    if delete_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp


# 投票
@web_ui.route('/api/registration/vote/<mobile>', methods=['GET'])
@login_required
@referrer
@cross_origin()
def registration_vote(mobile):
    vote_response = requests.get(API_URL + 'registration/vote/' + str(mobile), headers=get_headers())
    if vote_response.status_code == 200:
        resp = jsonify({'message': u'投票成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'投票失败！' if vote_response.json().get('message') is None else vote_response.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


# 投票
@web_ui.route('/api/registration/check/<mobile>', methods=['GET'])
@login_required
@referrer
@cross_origin()
def registration_check(mobile):
    vote_response = requests.get(API_URL + 'registration/check-mobile/' + str(mobile), headers=get_headers())
    if vote_response.status_code == 200:
        resp = jsonify({'message': u'该电话号码未参加活动！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'该电话号码已参加活动！'})
        resp.status_code = 400
        return resp


@web_ui.route('/api/registration/search-vote', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_registration_search_vote():
    registrations = []
    select_response = requests.get( API_URL + 'registration/search/vote', headers=get_headers())
    if select_response.status_code == 200:
        registrations = select_response.json()
    return Response(json.dumps(registrations))


# ------------------------------------------------ 第三方应用对接 start -----------------------------------------------------------------------


@web_ui.route('/api/external')
@login_required
@referrer
@cross_origin()
def api_external():
    return render_template('api_store/api_store.html', loginInfo=get_login_info(), showFlag='setting')

@web_ui.route('/api/externalAdmin')
@login_required
@referrer
@cross_origin()
def api_externalAdmin():
    return render_template('api_store/api_store_admin.html', loginInfo=get_login_info(), showFlag='setting')

@web_ui.route('/api/apis/save', methods=['POST'])
# @login_required
@referrer
@cross_origin()
def api_apis_save():
    data = json.loads(request.data)
    save_resp = requests.post(API_URL + 'apis/save', headers=get_headers(), data=json.dumps(data))
    if save_resp.status_code == 200:
        resp = jsonify({'message': u'保存成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'保存失败！'})
        resp.status_code = 400
        return resp


@web_ui.route('/api/apis/search-validity', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_apis_search_validity():
    api_store = []
    select_response = requests.get(
        API_URL + 'apis/search-validity',headers=get_headers())
    if select_response.status_code == 200:
        api_store = select_response.json()
    return Response(json.dumps(api_store))


@web_ui.route('/api/ex-applications/search-checked', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_exapplications_search_checked():
    checked_list = []
    select_response = requests.get(
        API_URL + 'ex-applications/search-checked',headers=get_headers())
    if select_response.status_code == 200:
        checked_list = select_response.json()
    return Response(json.dumps(checked_list))


@web_ui.route('/api/ex-applications/setting', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_exapplications_setting():
    data = json.loads(request.data)
    save_resp = requests.post(API_URL + 'ex-applications/setting', headers=get_headers(), data=json.dumps(data))
    if save_resp.status_code == 200:
        resp = jsonify({'message': u'设置成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'设置失败！'})
        resp.status_code = 400
        return resp

@web_ui.route('/api/apis/personal/save', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_apis_personal_save():
    data = json.loads(request.data)
    save_resp = requests.post(API_URL + 'apis/personal/save', headers=get_headers(), data=json.dumps(data))
    if save_resp.status_code == 200:
        resp = jsonify({'message': u'保存成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'保存失败！'})
        resp.status_code = 400
        return resp

@web_ui.route('/apis/personal/find-store', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_apis_personal_find_store():
    api_store = []
    select_response = requests.get(
        API_URL + 'apis/personal/find_personal_store', headers=get_headers())
    if select_response.status_code == 200:
        api_store = select_response.json()
    return Response(json.dumps(api_store))

@web_ui.route('/api/personal/switch/update', methods=['POST'])#关闭0    开启1
@login_required
@referrer
@cross_origin()
def api_personal_switch_update():
    data = json.loads(request.data)
    update_resp = requests.put(
        API_URL + 'apis/personal/' + str(data.get("apiPersonalStoreId")) + '/switch/' + str(data.get("status")) + '/update',
        headers=get_headers(), data=json.dumps(data))
    if update_resp.status_code == 200:
        resp = jsonify({'message': u'服务状态更新成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'服务状态更新失败！'})
        resp.status_code = 400
        return resp

# 根据id批量删除知识
@web_ui.route('/apis/personal/batchdelete', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_personal_store_batch_delete():
    request_data = json.loads(request.data)
    delete_response = requests.put(API_URL + 'apis/personal/batchdelete', headers=get_headers(),
                                 data=json.dumps(request_data))
    if delete_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp

@web_ui.route('/apis/personal/batchsubmit', methods=['POST'])
@login_required
@referrer
@cross_origin()
def api_personal_store_batch_submit():
    request_data = json.loads(request.data)
    delete_response = requests.put(API_URL + 'apis/personal/batchsubmit', headers=get_headers(),
                                       data=json.dumps(request_data))
    if delete_response.status_code == 200:
        resp = jsonify({'message': u'提交成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'提交失败！'})
        resp.status_code = 400
        return resp
#---站内信的一些接口

@web_ui.route('/mails/search', methods=['GET'])
@login_required
@referrer
@cross_origin()
def mails_search():
   save_response = requests.get(API_URL + 'mails/search', headers=get_headers())
   return Response(json.dumps(save_response.json()))



@web_ui.route('/mails/find_phone', methods=['GET'])
@login_required
@referrer
def mail_find_phone():
  # request_data = json.loads(request.data)
   save_response = requests.get(API_URL + 'mails/find_phone', headers=get_headers())
   return Response(json.dumps(save_response.json()))

@web_ui.route('/api/find/unread_letter_num', methods=['GET'])
@login_required
@cross_origin()
@referrer
def mail_find_unread_letter_num():
   save_response = requests.get(API_URL + 'mails/find/unread-letter-num', headers=get_headers())
   return Response(json.dumps(save_response.json()))

@web_ui.route('/mails/search_phone', methods=['GET'])
@login_required
@cross_origin()
@referrer
def mail_search_phone():
  # request_data = json.loads(request.data)
   page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
   save_response = requests.get(API_URL + 'mails/search_phone?pageNo='+page_no, headers=get_headers())
   return Response(json.dumps(save_response.json()))


@web_ui.route('/mails/save_phone', methods=['POST'])
@login_required
@referrer
@cross_origin()
def mails_save_phone():
    # print request
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'mails/save_phone', headers=get_headers(), data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': '保存成功！'})
        resp.status_code = 200
        return resp
    else:
        message = '保存失败！'
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp


@web_ui.route('/mails/find_p', methods=['POST'])
@login_required
@referrer
@cross_origin()
def mails_find_p():
    # print request
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'mails/find_p', headers=get_headers(), data=json.dumps(request_data))
    chat = save_response.json()
    return Response(json.dumps(chat))


#---粉丝的一些接口

@web_ui.route('/fans/save_fans', methods=['POST'])
@login_required
@referrer
@cross_origin()
def fans_save_fans():
    request_data = json.loads(request.data)
    chat_resp = requests.post(API_URL + 'fans/save_fans', headers=get_headers(),
                              data=json.dumps(request_data))
    chat = chat_resp.json()
    return Response(json.dumps(chat))







@web_ui.route('/fans/del_fans', methods=['POST'])
@login_required
@referrer
@cross_origin()
def fans_del_fans():
    request_data = json.loads(request.data)
    chat_resp = requests.post(API_URL + 'fans/del_fans', headers=get_headers(),
                              data=json.dumps(request_data))
    chat = chat_resp.json()
    return Response(json.dumps(chat))



@web_ui.route('/fans/find_fans', methods=['POST'])
@referrer  # 请求来源
@cross_origin()  # 转发
def fans_find_fans():
    request_data = json.loads(request.data)
    chat_resp = requests.post(API_URL + 'fans/find_fans', headers=get_headers(),
                              data=json.dumps(request_data))
    chat = chat_resp.json()
    return Response(json.dumps(chat))




#

# @web_ui.route('/fans/search_fans', methods=['POST'])
# @referrer  # 请求来源
# @cross_origin()  # 转发
# def fans_search_fans():
#     request_data = json.loads(request.data)
#     chat_resp = requests.post(API_URL + 'fans/search_fans', headers=get_headers(),
#                               data=json.dumps(request_data))
#     chat = chat_resp.json()
#     return Response(json.dumps(chat))


@web_ui.route('/fans/search_fans', methods=['GET'])
@referrer
@cross_origin()
def fans_search_fans():
   save_response = requests.get(API_URL + 'fans/search_fans', headers=get_headers())
   return Response(json.dumps(save_response.json()))



# @web_ui.route('/fans/search_count_fans', methods=['POST'])
# @referrer  # 请求来源
# @cross_origin()  # 转发
# @login_required
# def fans_search_count_fans():
#     request_data = json.loads(request.data)
#     chat_resp = requests.post(API_URL + 'fans/search_count_fans', headers=get_headers(),
#                               data=json.dumps(request_data))
#     chat = chat_resp.json()
#     return Response(json.dumps(chat))


@web_ui.route('/fans/search_count_fans', methods=['GET'])
@referrer
@cross_origin()
@login_required
def fans_search_count_fans():
   save_response = requests.get(API_URL + 'fans/search_count_fans', headers=get_headers())
   return Response(json.dumps(save_response.json()))



@web_ui.route('/fans/search_concern_list', methods=['GET'])
@referrer
@cross_origin()
@login_required
def fans_search_concern_list():
   save_response = requests.get(API_URL + 'fans/search_concern_list', headers=get_headers())
   return Response(json.dumps(save_response.json()))

@web_ui.route('/api/fans/search_fans_list', methods=['GET'])
@referrer
@cross_origin()
@login_required
def fans_search_fans_list():
   pageNo = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
   select_response = requests.get(API_URL + 'fans/login-user/search-page?pageNo='+pageNo, headers=get_headers())
   if select_response.status_code == 200:
       dictionaries = select_response.json()
   return Response(json.dumps(dictionaries))
   # return Response(json.dumps(save_response.json()))

@web_ui.route('/balance_search', methods=['GET'])
@referrer
@cross_origin()
@login_required
def fbalance_search():
    resp = jsonify({'balance': session.get('balance')})
    resp.status_code = 200
    return resp

@web_ui.route('/api/home/yesterday', methods=['GET'])
@referrer
@cross_origin()
@login_required
def home_yesterday_data():
   select_response = requests.get(API_URL + 'home/yesterday', headers=get_headers())
   if select_response.status_code == 200:
       dictionaries = select_response.json()
   return Response(json.dumps(dictionaries))

@web_ui.route('/api/home/yesterday/new_knowledge_num', methods=['GET'])
@referrer
@cross_origin()
@login_required
def home_yesterday_new_knowledge_num_data():
   select_response = requests.get(API_URL + 'home/yesterday/new-knowledge-num', headers=get_headers())
   if select_response.status_code == 200:
       dictionaries = select_response.json()
   return Response(json.dumps(dictionaries))

# --------------------------------------------------第三方应用对接 end ---------------------------------------------------------------------


# --------------------------------------------------微信公众号授权绑定 start ---------------------------------------------------------------------


@web_ui.route('/weixinApizys', methods=['POST'])
@referrer
@cross_origin()
def wechat_postinfo():
    obj = {}
    obj["param"] = request.data
    requests.post(API_URL + 'wechat/', headers=get_headers(), data=json.dumps(obj))
    return Response("success")


@web_ui.route('/api/wechat/authorize', methods=['POST'])
@referrer
@cross_origin()
def wechat_authorize():
    account = web_ui.get_account_info()
    response = requests.post(API_URL + 'wechat/authorize', headers=get_headers(), data=json.dumps(account))
    resp = jsonify({"message": response.text})
    resp.status_code = 200
    return resp


@web_ui.route('/redirect', methods=['GET'])
@referrer
@cross_origin()
def wechat_redirect():
    authorization = {}
    authorization["username"] = request.args["username"]
    authorization["auth_code"] = request.args["auth_code"]
    response = requests.post(API_URL + 'wechat/redirect', headers=get_headers(), data=json.dumps(authorization))
    if response.status_code == 200:
        return render_template('web/access_download.html', loginInfo=get_login_info(), showFlag='channel')


@web_ui.route('/weixinApi22/<appid>', methods=['POST'])
@referrer
@cross_origin()
def wechat_accept(appid):
    acceptParams = {}
    acceptParams["xml"] = request.data
    acceptParams["nonce"] = request.args["nonce"]
    acceptParams["timeStamp"] = request.args["timestamp"]
    acceptParams["msgSignature"] = request.args["msg_signature"]
    response = requests.post(API_URL + 'wechat/accept', headers=get_headers(), data=json.dumps(acceptParams))
    return Response(response.text)


# --------------------------------------------------微信公众号授权绑定 end ---------------------------------------------------------------------
# --------------------------------------------------微信扫码支付start---------------------------------------------------------------------------

@web_ui.route('/api/wechat/pay', methods=['POST'])
@login_required
@referrer
@cross_origin()
def wechat_pay():
    dataArray = {}
    localIP = socket.gethostbyname(socket.gethostname())  # 这个得到本地ip
    dataArray["localIP"]=localIP
    data = json.loads(request.data)
    dataArray["body"] =data.get("body")
    dataArray["order_price"]=data.get("order_price")
    dataArray["balance"] = data.get("balance")
    response = requests.post(API_URL + 'wechat/pay', headers=get_headers(), data=json.dumps(dataArray))
    return Response(response.text)

@web_ui.route('/wxscanpay', methods=['POST'])
@referrer
@cross_origin()
def wechat_scanpay():
    acceptParams = {}
    acceptParams["xml"] = request.data
    response = requests.post(API_URL + 'wechat/pay/scan', headers=get_headers(), data=json.dumps(acceptParams))
    print response.text
    return Response(response.text)
# --------------------------------------------------微信扫码支付end-----------------------------------------------------------------------------

# --------------------------------------------------第三方授权登录 start ---------------------------------------------------------------------


@web_ui.route('/api/third_bind/<idType>', methods=['POST'])
@referrer
@cross_origin()
def third_bind(idType):
    if idType != "angelId":
        resp = jsonify({'message': u'第三方账号类型错误'})
        resp.status_code = 400
        return resp
    request_data = json.loads(request.data)
    angelid = request_data.get(idType)
    angel_response = requests.get(API_URL + 'accounts/angelid/' + angelid, headers=get_headers())
    if angel_response.status_code == 200:
        resp = jsonify({'message': u'此天使号已绑定！'})
        resp.status_code = 400
        return resp
    login_response = requests.post(API_URL + 'accounts/login', headers=get_headers(), data=json.dumps(request_data))
    if login_response.status_code == 200:
        response = requests.post(API_URL + 'accounts/angel/bind', headers=get_headers(), data=json.dumps(request_data))
        if response.status_code == 200:
            account = login_response.json()
            create_session(account)
            resp = jsonify({'message': u'天使号绑定成功！'})
            resp.status_code = 200
            return resp
        else:
            resp = jsonify({'message': u'天使号绑定失败'})
            resp.status_code = 400
            return resp
    else:
        resp = jsonify({'message': u'可爱的小伙伴，您输入的帐户名或密码有误~~~'})
        resp.status_code = 400
        return resp

@web_ui.route('/api/third_login', methods=['POST'])
@referrer
@cross_origin()
def third_login():
    request_data = json.loads(request.data)
    idType = request_data.get("idType")
    if idType != "angelId":
        resp = jsonify({'message': u'第三方账号类型错误'})
        resp.status_code = 400
        return resp
    thirdId = request_data.get(idType)
    response = requests.get(API_URL + 'accounts/angelid/' + thirdId, headers=get_headers())
    if response.status_code == 200:
        account = response.json()
        create_session(account)
        username = account.get("userName")
        resp = jsonify({'username':username })
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'天使号登录失败！'})
        resp.status_code = 400
        return resp
# --------------------------------------------------第三方授权登录 end ---------------------------------------------------------------------



# -------------------------------------------------第三方接口 start------------------------------------------------------------------------
# 根据id查询知识信息
@web_ui.route('/api/app/info/search', methods=['GET'])
@login_required
@referrer
@cross_origin()
def api_get_app_info():
    appInfo = {}
    appInfo_response = requests.get(API_URL + 'app/info/search', headers=get_headers())
    if appInfo_response.status_code == 200:
        appInfo_response = appInfo_response.json()
    return Response(json.dumps(appInfo_response))

@web_ui.route('/api/app/robot_info/search/<unique_id>', methods=['GET'])
@referrer
@cross_origin()
def api_get_robot_info_info(unique_id):
    robot = get_company_robot_info(unique_id)
    return Response(json.dumps(robot))

# -------------------------------------------------第三方接口end---------------------------------------------------------------------------

# -------------------------------------------------告诉。。。：。。。 start------------------------------------------------------------------------
@web_ui.route('/tele_other/delete/<id>', methods=['POST'])
@referrer
@cross_origin()
def teleOther_delete_teleOther(id):
    tele_resp = requests.post(API_URL + 'teleOther/delete/'+id, headers=get_headers())
    if tele_resp.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp

@web_ui.route('/tele_other/search', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def teleOther_search_teleOther():
    teleOthers = {}
    receive_name = "" if request.args.get('receive_name') is None else request.args.get('receive_name')
    page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    select_response = requests.get(
        API_URL + 'teleOther/search?receive_name=' + receive_name+'&pageNo='+page_no,
        headers=get_headers())
    if select_response.status_code == 200:
        teleOthers = select_response.json()
    return Response(json.dumps(teleOthers))

@web_ui.route('/tele_other/<id>', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def teleOther_find_one_teleOther(id):
    request_data = json.loads(request.data)
    teleOther_resp = requests.get(API_URL + 'teleOther/'+id, headers=get_headers(),
                              data=json.dumps(request_data))
    teleOther = teleOther_resp.json()
    return Response(json.dumps(teleOther))

@web_ui.route('/tele_other/update', methods=['POST'])
@referrer  # 请求来源
@cross_origin()  # 转发
def teleOther_update():
    request_data = json.loads(request.data)
    teleOther_resp = requests.post(API_URL + 'teleOther/update', headers=get_headers(),
                                  data=json.dumps(request_data))
    teleOther = teleOther_resp.json()
    return Response(json.dumps(teleOther))
# -------------------------------------------------告诉。。。：。。。end---------------------------------------------------------------------------


# -------------------------------------------------恋爱机器人  开始---------------------------------------------------------------------------
@web_ui.route('/love/<id>', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_find_one_loveKnowledge(id):
    loveKnowledge_resp = requests.get(API_URL + 'love/'+id, headers=get_headers())
    loveKnowledge = loveKnowledge_resp.json()
    return Response(json.dumps(loveKnowledge))


@web_ui.route('/love/findAll', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_find_loveKnowledge():
    loveKnowledge_resp = requests.get(API_URL + 'love/findAll', headers=get_headers())
    loveKnowledge = loveKnowledge_resp.json()
    return Response(json.dumps(loveKnowledge))

@web_ui.route('/love/create', methods=['POST'])
@referrer
@cross_origin()
def love_create_loveKnowledge():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'love/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        loveKnowledge=register_resp.json();
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@web_ui.route('/loveMessageLog/create', methods=['POST'])
@referrer
@cross_origin()
def love_message_log_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'loveMessageLog/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        loveMessageLog=register_resp.json();
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@web_ui.route('/loveMessageLog/selectByUserId', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_message_log_select_user_id():
    love_message_log_resp = requests.get(API_URL + 'loveMessageLog/selectByUserId', headers=get_headers())
    love_message_log = love_message_log_resp.json()
    return Response(json.dumps(love_message_log))

@web_ui.route('/loveMessageLog/selectCountByUserId', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_message_log_select_count_user_id():
    love_message_log_resp = requests.get(API_URL + 'loveMessageLog/selectCountByUserId', headers=get_headers())
    love_message_log = love_message_log_resp.json()
    return Response(json.dumps(love_message_log))

@web_ui.route('/loveMessageLog/updateByUserId', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_message_log_update_by_user_id():
    love_message_log_resp = requests.get(API_URL + 'loveMessageLog/updateByUserId', headers=get_headers())
    love_message_log = love_message_log_resp.json()
    return Response(json.dumps(love_message_log))

@web_ui.route('/loveMessageLog/selectCountByReceiveId/<loveId>', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_message_log_select_count_receive_id(loveId):
    love_message_log_resp = requests.get(API_URL + 'loveMessageLog/selectCountByReceiveId/'+loveId, headers=get_headers())
    love_message_log = love_message_log_resp.json()
    return Response(json.dumps(love_message_log))

@web_ui.route('/loveMessageLog/selectCountGroupByReceiveId', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def love_message_log_select_count_groupby_receive_id():
    love_message_log_resp = requests.get(API_URL + 'loveMessageLog/selectCountGroupByReceiveId', headers=get_headers())
    love_message_log = love_message_log_resp.json()
    return Response(json.dumps(love_message_log))
# -------------------------------------------------恋爱机器人   end---------------------------------------------------------------------------

@web_ui.route('/get/brush/sheet/info', methods=['POST'])
@referrer
@cross_origin()
def remote_get_info():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL + 'brush/sheet/info', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        loveKnowledge = register_resp.json();
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

# -------------------------------------------------ditingPc1.0 start---------------------------------------------------------------------------
@web_ui.route('/base_table2')
@referrer
@login_required
@cross_origin()
def base_table2():
    return render_template('web/ditingpc1.0/base_table.html', loginInfo=get_login_info(), showFlag='base_table2')


@web_ui.route('/robot-setting')
@referrer
@login_required
@company_information
@cross_origin()
def robot_setting():
    user_agent = str(request.headers.get('User-Agent')).lower()
    if re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) :
        return render_template('web/phone/robotSet_phone.html', loginInfo=get_login_info(), showFlag='robot_setting')
    else:
        return render_template('web/ditingpc1.0/robotSet2.html', loginInfo=get_login_info(), showFlag='robot_setting')

@web_ui.route('/access_webVx1')
@referrer
@login_required
@key_information
@cross_origin()
def accessWebVx():
    return render_template('web/ditingpc1.0/access_webVx.html', loginInfo=get_login_info(), showFlag='access_webVx1')

@web_ui.route('/access_app2')
@referrer
@login_required
@key_information
@cross_origin()
def accessApp2():
    return render_template('web/ditingpc1.0/access_app.html', loginInfo=get_login_info(), showFlag='access_app2')

@web_ui.route('/access_api3')
@referrer
@login_required
@key_information
@cross_origin()
def accessApi3():
    return render_template('web/ditingpc1.0/access_api.html', loginInfo=get_login_info(), showFlag='access_api3')

@web_ui.route('/visitAdmin')
@referrer
@login_required
@cross_origin()
def visitAdmin():
    return render_template('web/ditingpc1.0/visitAdmin.html', loginInfo=get_login_info(), showFlag='visitAdmin')

@web_ui.route('/highQues')
@referrer
@login_required
@cross_origin()
def highQues():
    return render_template('web/ditingpc1.0/highQues.html', loginInfo=get_login_info(), showFlag='highQues')

@web_ui.route('/questions_count')
@referrer
@login_required
@cross_origin()
def questions_count():
    return render_template('web/ditingpc1.0/question_count.html', loginInfo=get_login_info(), showFlag='questions_count')

@web_ui.route('/talk_message')
@referrer
@login_required
@cross_origin()
def talk_message():
    return render_template('web/ditingpc1.0/talk_message.html', loginInfo=get_login_info(), showFlag='questions_count')

@web_ui.route('/apiSelfAdmin')
@login_required
@referrer
@cross_origin()
def apiSelfAdmin():
    return render_template('web/ditingpc1.0/apiSelfAdmin.html', loginInfo=get_login_info(), showFlag='apiSelfAdmin')

@web_ui.route('/apiThreeAdmin')
@login_required
@referrer
@cross_origin()
def apiThreeAdmin():
    return render_template('web/ditingpc1.0/apiThreeAdmin.html', loginInfo=get_login_info(), showFlag='apiThreeAdmin')

@web_ui.route('/apiPost')
@login_required
@referrer
@cross_origin()
def apiPost():
    return render_template('web/ditingpc1.0/apiPost.html', loginInfo=get_login_info(), showFlag='apiPost')

@web_ui.route('/knowledge-base')
@referrer
@login_required
@cross_origin()
def knowledge_base():
    return render_template('web/ditingpc1.0/knowledge_base.html', loginInfo=get_login_info(), showFlag='knowledge-base')

@web_ui.route('/invalidQuestion')
@referrer
@login_required
@cross_origin()
def invalidQuestion():
    return render_template('web/ditingpc1.0/invalid_question.html', loginInfo=get_login_info(), showFlag='invalidQuestion')

@web_ui.route('/Synonym2')
@referrer
@login_required
@cross_origin()
def Synonym2():
    return render_template('web/ditingpc1.0/Synonym.html', loginInfo=get_login_info(), showFlag='Synonym2')

@web_ui.route('/company-info')
@referrer
@login_required
@cross_origin()
def companySet():
    user_agent = str(request.headers.get('User-Agent')).lower()
    if re.findall(r'iphone|ipod|ipad|android.*mobile|windows.*phone|blackberry.*mobile', user_agent) :
       return render_template('web/phone/mySelf_phone.html', loginInfo=get_login_info())
    else:
        return render_template('web/ditingpc1.0/company_set.html', loginInfo=get_login_info(), showFlag='company-info2')

@web_ui.route('/pwd-change2')
@referrer
@login_required
@cross_origin()
def pwdReset2():
    return render_template('web/ditingpc1.0/pwdChange.html', loginInfo=get_login_info(), showFlag='pwd-change2')

@web_ui.route('/systemMessage')
@referrer
@login_required
@cross_origin()
def systemMessage2():
    robot = get_robot_info()
    return render_template('web/ditingpc1.0/system_message.html', loginInfo=get_login_info(),robot=robot, showFlag='systemMessage')

@web_ui.route('/privateMessage')
@referrer
@login_required
@cross_origin()
def privateMessage2():
    robot = get_robot_info()
    return render_template('web/ditingpc1.0/private_message.html', loginInfo=get_login_info(),robot=robot, showFlag='privateMessage')

@web_ui.route('/add_quesK2')
# @referrer
# @cross_origin()
def addQuesK2():
    return render_template('web/ditingpc1.0/add_quesK.html')

@web_ui.route('/invalid_quesK2')
# @referrer
# @cross_origin()
def invalidQuesK2():
    return render_template('web/ditingpc1.0/invalid_quesK.html')

@web_ui.route('/add_synonym2')
# @referrer
# @cross_origin()
def addSynonym2():
    return render_template('web/ditingpc1.0/add_synonym.html')

@web_ui.route('/leading_quesK2')
# @referrer
# @cross_origin()
def leadingQuesK2():
    return render_template('web/ditingpc1.0/leading_quesK.html')

# -------------------------------------------------ditingPc1.0   end---------------------------------------------------------------------------

# -------------------------------------------------www.dtrobo.com   start---------------------------------------------------------------------------
@web_ui.route('/dtrobo_index')
@referrer
@cross_origin()
def dtrobo_index():
    return render_template('web/dtrobo/dtrobo_index.html',loginInfo=get_login_info())

@web_ui.route('/dtrobo_robotApp')
@referrer
@cross_origin()
def dtrobo_robotApp():
    return render_template('web/dtrobo/dtrobo_robotApp.html')

@web_ui.route('/dtrobo_blackPic')
@referrer
@cross_origin()
def dtrobo_blackPic():
    return render_template('web/dtrobo/dtrobo_blackPic.html')

@web_ui.route('/dtrobo_robotWX')
@referrer
@cross_origin()
def dtrobo_robotWX():
    return render_template('web/dtrobo/dtrobo_robotWX.html')

# ------------------------------------------------- www.dtrobo.com   end--------------------------------------------------------------------------

# ------------------------------------------------- 用户后台统计 start--------------------------------------------------------------------------
@web_ui.route('/api/statistical-data/all', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def chat_sta_all_username():
    chat_sta_resp = requests.get(API_URL + 'chat/record/sta/search-question-count/all', headers=get_headers())
    chat_sta = chat_sta_resp.json()
    return Response(json.dumps(chat_sta))

@web_ui.route('/chat/record/sta/search-uuid-count/all', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def chat_sta_uuid_all_username():
    chat_sta_uuid_resp = requests.get(API_URL + 'chat/record/sta/search-uuid-count/all', headers=get_headers())
    chat_sta = chat_sta_uuid_resp.json()
    return Response(json.dumps(chat_sta))

@web_ui.route('/chat/record/sta/search-uuid-count/type', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def chat_sta_uuid_all_username_type():
    type = "" if request.args.get('type') is None else request.args.get('type')
    starttime = "" if request.args.get('beginTime') is None else request.args.get('beginTime')
    endtime = "" if request.args.get('endTime') is None else request.args.get('endTime')
    chat_sta_uuid_resp = requests.get(API_URL + 'chat/record/sta/search-uuid-count/type?type='+type+"&starttime="+starttime+"&endtime="+endtime, headers=get_headers())
    chat_sta = chat_sta_uuid_resp.json()
    return Response(json.dumps(chat_sta))

@web_ui.route('/api/chat/record/search-username-count', methods=['GET'])
@referrer  # 请求来源
@cross_origin()  # 转发
def chat_sta_username_type():
    type = "" if request.args.get('type') is None else request.args.get('type')
    starttime = "" if request.args.get('beginTime') is None else request.args.get('beginTime')
    endtime = "" if request.args.get('endTime') is None else request.args.get('endTime')
    chat_sta_uuid_resp = requests.get(
        API_URL + 'chat/record/sta/search-username-count/type?type=' + type + "&starttime=" + starttime + "&endtime=" + endtime,
        headers=get_headers())
    chat_sta = chat_sta_uuid_resp.json()
    return Response(json.dumps(chat_sta))
# -------------------------------------------------用户后台统计   end---------------------------------------------------------------------------

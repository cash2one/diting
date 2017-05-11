# -*-coding: utf-8-*-
import sys
from functools import wraps
from flask import Flask, Blueprint, render_template, request, redirect, session, Response, jsonify
from settings import API_URL

from common import get_headers, THREAD_LOCAL
from flask_cors import cross_origin

import requests
import json

reload(sys)
sys.setdefaultencoding("utf-8")

web_antusheng_ui = Blueprint('web_antusheng_ui', __name__)
app = Flask(__name__)

@web_antusheng_ui.route('/')
@cross_origin()
def home_page():
    return render_template('web/antushengPc1.0/login.html')

@web_antusheng_ui.route('/atsIndex')
@cross_origin()
def atsIndex():
    return render_template('web/antushengPc1.0/indexPage.html')

@web_antusheng_ui.route('/atsLogin')
@cross_origin()
def atsLogin():
    return render_template('web/antushengPc1.0/atsLogin.html')

@web_antusheng_ui.route('/atsRegister')
@cross_origin()
def atsRegister():
    return render_template('web/antushengPc1.0/atsRegister.html')

@web_antusheng_ui.route('/atsForget')
@cross_origin()
def atsForget():
    return render_template('web/antushengPc1.0/atsForgetPsd.html')

def referrer(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        THREAD_LOCAL.referrer = 'PC'
        return f(*args, **kwargs)

    return decorated_function

def create_session(internal):
    session['username'] = internal.get('username')

@web_antusheng_ui.route('/login', methods=['POST'])
@referrer
@cross_origin()
def login():
    request_data = json.loads(request.data)
    login_response = requests.post(API_URL + 'internal/login', headers=get_headers(), data=json.dumps(request_data))
    if login_response.status_code == 200:
        internal = login_response.json()
        create_session(internal)
        resp = jsonify({'message': u'登录成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'可爱的小伙伴，您输入的帐户名或密码有误~~~'})
        resp.status_code = 400
        return resp




@web_antusheng_ui.route('/api/reset/captchas/mobile/<mobile>', methods=['GET'])
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

@web_antusheng_ui.route('/api/register/captchas/mobile/<mobile>', methods=['GET'])
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


@web_antusheng_ui.route('/api/accounts/register/mobile', methods=['POST'])
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

@web_antusheng_ui.route('/api/password/reset', methods=['POST'])
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





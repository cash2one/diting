# -*-coding: utf-8-*-
import sys
from functools import wraps
import xlrd
from flask import Flask, abort, Blueprint, render_template, request, redirect, session, Response, jsonify
from settings import API_URL

from common import get_headers, THREAD_LOCAL
from flask_cors import cross_origin

import requests
import json

import web_ui

reload(sys)
sys.setdefaultencoding("utf-8")

wechat_ui = Blueprint('wechat_ui', __name__)
app = Flask(__name__)


def referrer(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        THREAD_LOCAL.referrer = 'WECHAT'
        return f(*args, **kwargs)

    return decorated_function


def create_session(account):
    session['user_id'] = account.get('id')
    session['user_type'] = u'DITING_USER'
    session['user_name'] = account.get('userName')
    session['unique_id'] = get_robot_info().get('uniqueId')
    session['domain_name'] = get_robot_info().get('shortDomainName')


def get_robot_info():
    robot = {}
    robot_response = requests.get(API_URL + 'robots/user/' + str(session.get('user_id')), headers=get_headers())
    if robot_response.status_code == 200:
        robot = robot_response.json()
    return robot


def get_login_info():
    loginInfo = {}
    loginInfo['userType'] = session.get('user_type')
    loginInfo['userName'] = session.get('user_name')
    loginInfo['uniqueId'] = session.get('unique_id')
    loginInfo['shortDomainName'] = session.get('domain_name')
    return loginInfo


@wechat_ui.route('/')
@referrer
@cross_origin()
def home_page():
    return render_template('web/ditingpc1.0/index.html')

@wechat_ui.route('/weixinApizys',methods=['POST'])
@referrer
@cross_origin()
def wechat_postinfo():
    print request
    print request.data
    obj={}
    obj["param"] = request.data
    requests.post(API_URL + 'wechat/', headers=get_headers(), data=json.dumps(obj))
    return Response("success")

@wechat_ui.route('/api/wechat/authorize',methods=['POST'])
@referrer
@cross_origin()
def wechat_authorize():
    account = web_ui.get_account_info()
    response = requests.post(API_URL + 'wechat/authorize', headers=get_headers(), data=json.dumps(account))
    resp = jsonify({"message": response.text})
    resp.status_code = 200
    return resp

@wechat_ui.route('/redirect',methods=['GET'])
@referrer
@cross_origin()
def wechat_redirect():
    authorization = {}
    authorization["username"] = request.args["username"]
    authorization["auth_code"] = request.args["auth_code"]
    response = requests.post(API_URL + 'wechat/redirect', headers=get_headers(), data=json.dumps(authorization))
    if response.status_code == 200:
      return render_template('web/ditingpc1.0/access_webVx1.html', loginInfo=web_ui.get_login_info())

@wechat_ui.route('/weixinApi22/<appid>',methods=['POST'])
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


@wechat_ui.route('/wechat_login', methods=['GET'])
@referrer
@cross_origin()
def wechat_login():
    code = request.args["code"]
    appId = "wxd7e83c7f47dab038"
    appsecret = "86d4caeefa7c609f4175842ce64443a3"
    url_get_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code"
    response = requests.get(url_get_access_token,headers={}, verify=False)
    if response.status_code == 200:
        authorInfo = response.json()
        openId = authorInfo.get("openid")
        unionId = authorInfo.get("unionid")
        wechatInfo = {}
        wechatInfo['openId'] = openId
        wechatInfo['unionId'] = unionId
        response = requests.post(API_URL + 'accounts/wechat', headers=get_headers(),data=json.dumps(wechatInfo))
        if response.status_code == 200:
            account = response.json()
            create_session(account)
            return render_template('web/ditingpc1.0/success_index.html', loginInfo=get_login_info())
        else:
            return render_template('web/binding_login.html',wechatInfo = wechatInfo)


@wechat_ui.route('/api/wechat_bind', methods=['POST'])
@referrer
@cross_origin()
def wechat_bind():
    request_data = json.loads(request.data)
    login_response = requests.post(API_URL + 'accounts/login', headers=get_headers(), data=json.dumps(request_data))
    if login_response.status_code == 200:
        response = requests.post(API_URL + 'accounts/wechat/bind', headers=get_headers(), data=json.dumps(request_data))
        if response.status_code == 200:
            account = login_response.json()
            create_session(account)
            resp = jsonify({'message': u'绑定微信成功！'})
            resp.status_code = 200
            return resp
        else:
            resp = jsonify({'message': u'绑定微信失败'})
            resp.status_code = 400
            return resp
    else:
        resp = jsonify({'message': u'可爱的小伙伴，您输入的帐户名或密码有误~~~'})
        resp.status_code = 400
        return resp


@wechat_ui.route('/api/remote/wechat_login', methods=['POST'])
@referrer
@cross_origin()
def remote_wechat_login():
    request_data = json.loads(request.data)
    response = requests.post(API_URL + 'accounts/wechat', headers=get_headers(), data=json.dumps(request_data))
    if response.status_code == 200:
        account = response.json()
        create_session(account)
        resp = jsonify({'message': u'登录成功'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'账号不存在'})
        resp.status_code = 400
        return resp



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

agent_ui = Blueprint('agent_ui', __name__)
app = Flask(__name__)


def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if (session.get('user_id') is None or session.get('user_type') != u'AGENT'):
            return redirect('/login')
        return f(*args, **kwargs)

    return decorated_function


def referrer(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        THREAD_LOCAL.referrer = 'PC-AGNT'
        return f(*args, **kwargs)

    return decorated_function


@agent_ui.route('/login')
@referrer
@cross_origin()
def login():
    return render_template('web/agent/agent_login.html')


@agent_ui.route('/agent_admin')
@login_required
@referrer
@cross_origin()
def agent_admin():
    return render_template('web/agent/agent_admin.html', loginInfo=get_login_info(), showFlag='E')

@agent_ui.route('/agent_invitation')
@login_required
@referrer
@cross_origin()
def agent_invitation():
    return render_template('web/agent/agent_invitation.html', loginInfo=get_login_info(), showFlag='S')


@agent_ui.route('/api/login', methods=['POST'])
@referrer
@cross_origin()
def api_login():
    request_data = json.loads(request.data)
    login_response = requests.post(API_URL + 'agents/login', headers=get_headers(), data=json.dumps(request_data))
    if login_response.status_code == 200:
        agent = login_response.json()
        create_session(agent)
        resp = jsonify({'message': u'登录成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'可爱的小伙伴，您输入的帐户名或密码有误~~~'})
        resp.status_code = 400
        return resp


@agent_ui.route('/logout')
@referrer
@cross_origin()
def logout():
    session['user_id'] = None
    session['user_type'] = ""
    session['user_name'] = ""
    return redirect("/dl/login")

@agent_ui.route('/api/agent/companies/search-page', methods=['GET'])
@referrer
@cross_origin()
def api_agent_companies_searchpage():
    dictionaries = []
    page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    invitation_code = "" if request.args.get('invitationCode') is None else request.args.get('invitationCode')
    select_response = requests.get(API_URL + 'companies/egent/search-page?pageNo=' + page_no+'&invitationCode='+invitation_code, headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))

def create_session(agent):
    session['user_id'] = agent.get('id')
    session['user_type'] = u'AGENT'
    session['user_name'] = agent.get('userName')
    session['invitation_code'] = agent.get('invitationCode')


def get_login_info():
    loginInfo = {}
    loginInfo['userType'] = session.get('user_type')
    loginInfo['userName'] = session.get('user_name')
    loginInfo['invitationCode'] = session.get('invitation_code')
    return loginInfo



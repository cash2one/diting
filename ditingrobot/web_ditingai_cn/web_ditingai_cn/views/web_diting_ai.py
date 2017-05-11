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

web_ditingai_ui = Blueprint('web_ditingai_ui', __name__)
app = Flask(__name__)

@web_ditingai_ui.route('/')
@cross_origin()
def home_page():
    return render_template('login.html')


def referrer(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        THREAD_LOCAL.referrer = 'PC'
        return f(*args, **kwargs)

    return decorated_function


@web_ditingai_ui.route('/login', methods=['POST'])
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

def create_session(internal):
    session['username'] = internal.get('username')





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

segmentation_ui = Blueprint('segmentation_ui', __name__)
app = Flask(__name__)

#
# def login_required(f):
#     @wraps(f)
#     def decorated_function(*args, **kwargs):
#         if (session.get('user_id') is None or session.get('user_type') != u'AGENT'):
#             return redirect('/login')
#         return f(*args, **kwargs)
#
#     return decorated_function
#
#
# def referrer(f):
#     @wraps(f)
#     def decorated_function(*args, **kwargs):
#         THREAD_LOCAL.referrer = 'PC-SEG'
#         return f(*args, **kwargs)
#
#     return decorated_function

@segmentation_ui.route('/')
@cross_origin()
def home_page():
    return render_template('seg/login.html')

@segmentation_ui.route('/admin')
@cross_origin()
def seg_admin():
    return render_template('seg/seg_admin.html')

@segmentation_ui.route('/native')
@cross_origin()
def seg_native():
    return render_template('seg/native.html')

@segmentation_ui.route('/synonymous')
@cross_origin()
def seg_synonymous():
    return render_template('seg/synonymous.html')

@segmentation_ui.route('/absoluteReplacement')
@cross_origin()
def seg_absoluteReplacement():
    return render_template('seg/absoluteReplacement.html')

@segmentation_ui.route('/synonymy')
@cross_origin()
def seg_synonymy():
    return render_template('seg/synonymy.html')

@segmentation_ui.route('/spokenLanguage')
@cross_origin()
def seg_spokenLanguage():
    return render_template('seg/spokenLanguage.html')

@segmentation_ui.route('/newWord')
@cross_origin()
def seg_newWord():
    return render_template('seg/newWord.html')

@segmentation_ui.route('/semanticsAPI')
@cross_origin()
def seg_semanticsAPI():
    return render_template('seg/semanticsAPI.html')


def referrer(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        THREAD_LOCAL.referrer = 'PC'
        return f(*args, **kwargs)

    return decorated_function


@segmentation_ui.route('/login', methods=['POST'])
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





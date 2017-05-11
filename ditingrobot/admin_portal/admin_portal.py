# -*-coding: utf-8-*-
import os
from functools import wraps
from flask import Flask, render_template, redirect, session, abort, request, jsonify, Response
from flask_cors import cross_origin
from tornado.wsgi import WSGIContainer
from tornado.httpserver import HTTPServer
from tornado.ioloop import IOLoop
from flask import make_response

import time
import datetime
import base64
import hashlib
import json
import requests
from xlwt import Workbook

import config
import sys
import qiniu_image

reload(sys)
sys.setdefaultencoding("utf-8")

app = Flask(__name__)

API_URL = config.HOST_ADDR + ':9090/'
APP_KEY = '!(*@)^#$%)!@&*$)'
GOD_MODE = False


def get_headers():
    name = session.get('employee_name')
    if name:
        name = base64.b64encode(name)

    return {
        'Content-Type': 'application/json',
        'X-USER-NAME-BASE64': name,
        'X-USER-ID': session.get('employee_id'),
        'X-USER-TYPE': 'EMPLOYEE_USER',
        'X-APP-KEY': APP_KEY,
        'X-REFERRER': 'PC-MANAGE'
    }


def play_as_god():
    if (session.get('employee_id') is None):
        session['employee_id'] = 1
        session['employee_name'] = 'God'
        session['employee_roles'] = ['ADMIN']


def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        # if GOD_MODE:
        #     play_as_god()

        if (session.get('employee_id') is None):
            return redirect('/login')
        return f(*args, **kwargs)

    return decorated_function


def check_role(*roles):
    if GOD_MODE:
        return

    if (session['employee_roles'] is None):
        abort(403)

    result = set(roles) & set(session['employee_roles'])
    if (not result):
        abort(403)


@app.route('/')
@app.route('/login')
@cross_origin()
def login():
    return render_template('login.html')


@app.route('/api/login', methods=['POST'])
@cross_origin()
def api_login():
    pass
    request_data = json.loads(request.data)
    login_response = requests.post(API_URL + 'employees/login', headers=get_headers(), data=json.dumps(request_data))
    if login_response.status_code == 200:
        employee = login_response.json()

        create_session(employee)

        resp = jsonify({'message': u'登录成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'可爱的小伙伴，您输入的帐户名或密码有误~~~'})
        resp.status_code = 400
        return resp


@app.route('/logout')
@cross_origin()
def logout():
    session['employee_id'] = None
    session['user_type'] = ""
    return redirect("/login")


@app.route('/knowledge/a')
@login_required
@cross_origin()
def knowledge_a():
    return render_template('knowledge_a.html', loginInfo=get_login_info(),showFlag = 'A')

@app.route('/adminIndex')
@login_required
@cross_origin()
def adminIndex():
    return render_template('admin2.0/index.html', loginInfo=get_login_info())

@app.route('/adminRepository')
@login_required
@cross_origin()
def adminRepository():
    return render_template('admin2.0/repository.html', loginInfo=get_login_info())

@app.route('/adminAgency')
@login_required
@cross_origin()
def adminAgency():
    return render_template('admin2.0/agency.html', loginInfo=get_login_info())

@app.route('/adminFeedback')
@login_required
@cross_origin()
def adminFeedback():
    return render_template('admin2.0/feedback.html', loginInfo=get_login_info())

@app.route('/adminAppManager')
@login_required
@cross_origin()
def adminAppManager():
    return render_template('admin2.0/appManager.html', loginInfo=get_login_info())

@app.route('/adminDataManager')
@login_required
@cross_origin()
def adminDataManager():
    return render_template('admin2.0/dataManager.html', loginInfo=get_login_info())

@app.route('/adminUserManager')
@login_required
@cross_origin()
def adminUserManager():
    return render_template('admin2.0/userManager.html', loginInfo=get_login_info())

# select knowledge_a
@app.route('/api/base/knowledge/search-page', methods=['GET'])
@login_required
@cross_origin()
def api_base_knowledge_searchpage():
    dictionaries = []
    type = "0" if request.args.get('type') is None else request.args.get('type')
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    pageSize = "" if request.args.get('pageSize') is None else request.args.get('pageSize')
    company_id = "" if request.args.get('companyId') is None else request.args.get('companyId')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    query_state = "" if request.args.get('queryState') is None else request.args.get('queryState')
    query_criteria = "" if request.args.get('queryCriteria') is None else request.args.get('queryCriteria')
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    select_response = requests.get(API_URL + 'knowledge/admin/search-page?pageNo=' + page_no+'&pageSize=' +pageSize+'&companyId='+ company_id+'&keywords='+keywords+'&queryState='+query_state+'&queryCriteria='+query_criteria+ '&starttime=' + starttime + '&endtime=' + endtime+'&type='+type, headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))

@app.route('/api/companies/admin/search-page', methods=['GET'])
@login_required
@cross_origin()
def api_companies_admin_searchpage():
    dictionaries = []
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    type = "0" if request.args.get('type') is None else request.args.get('type')
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    print starttime
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    source = "0" if request.args.get('source') is None else request.args.get('source')
    select_response = requests.get(API_URL + 'companies/admin/search-page?pageNo=' + page_no+'&keyword='+keyword+'&type='+type+ '&starttime=' + starttime + '&endtime=' + endtime+'&source='+source, headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))

# select knowledge_b
@app.route('/api/company/knowledge/search-page', methods=['GET'])
@login_required
@cross_origin()
def api_company_knowledge_searchpage():
    dictionaries = []
    type = "0" if request.args.get('type') is None else request.args.get('type')
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    company_id = "" if request.args.get('companyId') is None else request.args.get('companyId')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    query_state = "" if request.args.get('queryState') is None else request.args.get('queryState')
    query_criteria = "" if request.args.get('queryCriteria') is None else request.args.get('queryCriteria')
    select_response = requests.get(API_URL + 'knowledge/admin/search-company-page?pageNo=' + page_no+'&companyId='+ company_id+'&keywords='+keywords+'&queryState='+query_state+'&queryCriteria='+query_criteria+'&type='+type, headers=get_headers())
    if select_response.status_code == 200:
        dictionaries = select_response.json()
    return Response(json.dumps(dictionaries))

# @app.route('/api/company/knowledges/search-page', methods=['GET'])
# @login_required
# @cross_origin()
# def api_company_knowledges_searchpage():
#     dictionaries = []
#     page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
#     company_id = "" if request.args.get('companyId') is None else request.args.get('companyId')
#     keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
#     query_state = "" if request.args.get('queryState') is None else request.args.get('queryState')
#     query_criteria = "" if request.args.get('queryCriteria') is None else request.args.get('queryCriteria')
#     select_response = requests.get(API_URL + 'knowledge/admin/search-company-page?pageNo=' + page_no+'&companyId='+ company_id+'&keywords='+keywords+'&queryState='+query_state+'&queryCriteria='+query_criteria, headers=get_headers())
#     if select_response.status_code == 200:
#         dictionaries = select_response.json()
#     return Response(json.dumps(dictionaries))


@app.route('/knowledge/b',methods=['GET'])
@login_required
@cross_origin()
def knowledge_b():
    companyId = "" if request.args.get('company') is None else request.args.get('company')
    # companyid=jsonify({'companyId': companyId})
    # print companyid
    return render_template('knowledge_b.html', loginInfo=get_login_info(),showFlag = 'B',companyId=companyId)

def get_knowledge_b(companyId):
    company_knowledge = {}
    robot_response = requests.get(API_URL + 'knowledge/admin/search-company-page?companyId=' + companyId, headers=get_headers())
    if robot_response.status_code == 200:
        company_knowledge = robot_response.json()
    return company_knowledge

# 新增企业知识库
@app.route('/api/knowledge/admin/save', methods=['POST'])
@login_required
@cross_origin()
def api_company_knowledge_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/admin/create', headers=get_headers(), data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'添加失败！'})
        resp.status_code = 400
        return resp

# 新增管理员意见反馈
@app.route('/api/message/feedback_message/create', methods=['POST'])
@login_required
@cross_origin()
def api_feedback_message_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'message/feedback_message/create', headers=get_headers(), data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'添加失败！'})
        resp.status_code = 400
        return resp

# 管理员意见反馈查询
@app.route('/api/search-message/<id>', methods=['GET'])
@login_required
@cross_origin()
def api_search_message(id):
    message_response = requests.get(API_URL + 'message/search-message/'+id, headers=get_headers())
    if message_response.status_code == 200:
        message = message_response.json()
        # return Response(json.dumps(message))
        return render_template('view_agree.html', loginInfo=get_login_info(), message=message)
    else:
        resp = jsonify({'message': u'查询异常！'})
        resp.status_code = 400
        return resp


@app.route('/api/admin/upload', methods=['GET', 'POST'])
@login_required
@cross_origin()
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
        batchdelete_invalidquestions_response = requests.get(API_URL + 'knowledge/admin/upload?upload_path=' + upload_path,
                                                             headers=get_headers())
    return json.dumps(batchdelete_invalidquestions_response.content)

#图片上传
@app.route('/upload/<element>', methods=['POST'])
@cross_origin()
def upload_img(element):
    error = ''
    url = ''
    # 要上传的空间
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

# 根据id批量删除知识
@app.route('/api/knowledge/admin/batchdelete', methods=['POST'])
@login_required
@cross_origin()
def api_company_knowledge_batch_delete():
    request_data = json.loads(request.data)
    save_response = requests.put(API_URL + 'knowledge/admin/batchdelete', headers=get_headers(),
                                 data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp


# 知识修改
@app.route('/api/admin/knowledge/update', methods=['POST'])
@login_required
@cross_origin()
def api_company_knowledge_update():
    request_data = json.loads(request.data)

    update_response = requests.put(API_URL + 'knowledge/admin/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'知识修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'知识修改失败！'})
        resp.status_code = 400
        return resp

# 根据id删除知识
@app.route('/api/knowledge/admin/delete/<knowledgeId>', methods=['POST'])
@login_required
@cross_origin()
def api_base_knowledge_delete(knowledgeId):
    save_response = requests.put(API_URL + 'knowledge/delete/' + knowledgeId, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp

# get scene
@app.route('/api/admin/knowledge/scene', methods=['POST'])
@cross_origin()
def api_admin_get_scene():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'knowledge/scene', headers=get_headers(), data=json.dumps(request_data))
    return Response(json.dumps(save_response.json()))

@app.route('/invalid_question')
@login_required
@cross_origin()
def invalid_question():
    return render_template('invalid_question.html', loginInfo=get_login_info(),showFlag = 'Q')


@app.route('/mails/search')
@login_required
@cross_origin()
def mails_search():
   save_response = requests.get(API_URL + 'mails/search', headers=get_headers())
   return Response(json.dumps(save_response.json()))

#无效问题记录查询
@app.route('/api/admin/invalidquestion/searchpage',methods=['GET'])
@login_required
@cross_origin()
def api_admin_invalidquestion_searchpage():
    invalids = {}
    pageNo = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    select_invalidquestions_response = requests.get(API_URL + 'chat/record/admin/invalid-question/search-page?pageNo=' +pageNo+ '&keywords=' + keywords, headers=get_headers())
    if select_invalidquestions_response.status_code == 200:
        invalids = select_invalidquestions_response.json()
    return Response(json.dumps(invalids))

@app.route('/api/admin/message/searchpage',methods=['GET'])
@login_required
@cross_origin()
def api_admin_message_searchpage():
    invalids = {}
    pageNo = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    # keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    select_message_response = requests.get(API_URL + 'message/search-page?pageNo=' +pageNo, headers=get_headers())
    if select_message_response.status_code == 200:
        invalids = select_message_response.json()
    return Response(json.dumps(invalids))

# 用户反馈意见批量删除
@app.route('/api/admin/message/batchdelete', methods=['GET'])
@login_required
@cross_origin()
def api_message_batchdelete():
    id_str = "" if request.args.get('id_str') is None else request.args.get('id_str')
    ids = id_str.replace("|", "&")
    batchdelete_message_response = requests.get(
        API_URL + 'message/batchdelete?' + ids, headers=get_headers())
    if batchdelete_message_response.status_code == 200:
        resp = jsonify({'message': u'批量删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'批量删除失败！'})
        resp.status_code = 400
        return resp

@app.route('/business_admin')
@login_required
@cross_origin()
def business_admin():
    return render_template('business_admin.html', loginInfo=get_login_info(),showFlag = 'E')

@app.route('/admin_agent')
@login_required
@cross_origin()
def admin_agent():
    return render_template('admin_agent.html', loginInfo=get_login_info())


@app.route('/activity_page')
# @login_required
@cross_origin()
def activity_page():
    return render_template('activity_page.html')


@app.route('/registration/user/search', methods=['GET'])
@cross_origin()
def api_registration_user_search():
    registration_user = {}
    search_response = requests.get(API_URL + 'registration/search', headers=get_headers())
    if search_response.status_code == 200:
        registration_user = search_response.json()
    return Response(json.dumps(registration_user))

@app.route('/registration/user/create', methods=['POST'])
@cross_origin()
def api_registration_user_save():
    request_data = json.loads(request.data)
    save_response = requests.post(API_URL + 'registration/create', headers=get_headers(), data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'添加失败！'})
        resp.status_code = 400
        return resp

@app.route('/add_quesK_a')
@login_required
@cross_origin()
def add_quesK_a():
    return render_template('add_quesK_a.html')



@app.route('/leading_quesK_a')
@login_required
@cross_origin()
def leading_quesK_a():
    return render_template('leading_quesK_a.html')

@app.route('/daily_table')
@login_required
@cross_origin()
def daily_table():
    return render_template('daily_table.html',loginInfo=get_login_info(),showFlag = 'D')

@app.route('/view_tickling')
@login_required
@cross_origin()
def view_tickling():
    return render_template('view_tickling.html',loginInfo=get_login_info())

@app.route('/view_agree')
@login_required
@cross_origin()
def view_agree():
    id = "" if request.args.get('id') is None else request.args.get('id')
    return render_template('view_agree.html',loginInfo=get_login_info(),id=id)


@app.route('/api/external/admin')
@login_required
@cross_origin()
def api_external_admin():
    return render_template('admin_store.html',loginInfo=get_login_info(),showFlag = 'S')


@app.route('/api/apis/search-page', methods=['GET'])
@login_required
@cross_origin()
def api_apis_searchpage():
    apis = {}
    page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    resp = requests.get(API_URL + 'apis/search-page?pageNo='+str(page_no), headers=get_headers())
    if resp.status_code == 200:
        apis = resp.json()
    return Response(json.dumps(apis))

@app.route('/api/sta-accounts/search-page', methods=['GET'])
@login_required
@cross_origin()
def api_staAccounts_searchpage():
    staAccounts = []
    page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    resp = requests.get(API_URL + 'sta-accounts/search-page?pageNo='+str(page_no), headers=get_headers())
    if resp.status_code == 200:
        staAccounts = resp.json()
    return Response(json.dumps(staAccounts))

@app.route('/api/sta-accounts/export-sta-account', methods=['GET'])
@cross_origin()
def api_staAccounts_export_staAccount():
    staAccounts_responses = requests.get(
        API_URL + 'sta-accounts/export-sta-account', headers=get_headers())
    file_path = generate_excel(staAccounts_responses)

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

# 基础知识库批量导出
@app.route('/api/base/exportknowledge', methods=['GET'])
@cross_origin()
def down_base_knowledge():
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    type = "0" if request.args.get('type') is None else request.args.get('type')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    query_state = "" if request.args.get('queryState') is None else request.args.get('queryState')
    knowledge_responses = requests.get(
        API_URL + 'knowledge/admin/exportknowledges?starttime=' + starttime + '&endtime=' + endtime+'&type='+type+'&queryState='+query_state+'&keywords='+keywords, headers=get_headers())
    file_path = generate_knowledge_excel(knowledge_responses)

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

@app.route('/api/switch/update', methods=['POST'])#关闭0    开启1
@login_required
@cross_origin()
def api_switch_update():
    data = json.loads(request.data)
    update_resp = requests.put(
        API_URL + 'apis/' + str(data.get("apiStoreId")) + '/switch/' + str(data.get("status")) + '/update',
        headers=get_headers(), data=json.dumps(data))
    if update_resp.status_code == 200:
        resp = jsonify({'message': u'服务状态更新成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'服务状态更新失败！'})
        resp.status_code = 400
        return resp


@app.route('/api/accounts/invitation-code', methods=['GET'])
@cross_origin()
def get_invitation_code():
    mobile = "" if request.args.get('mobile') is None else request.args.get('mobile')

    invitation_resp = requests.get(API_URL + 'accounts/invitation-code/' + str(mobile), headers=get_headers())

    if invitation_resp.status_code == 200:
        resp = jsonify({'message': u'获取邀请码成功！', 'invitationCode': invitation_resp.json().get('invitationCode')})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'获取邀请码失败！'})
        resp.status_code = 400
        return resp


@app.route('/api/approval/update', methods=['POST'])#审批通过1     不通过0
@login_required
@cross_origin()
def api_approval_update():
    data = json.loads(request.data)
    update_resp = requests.put(
        API_URL + 'apis/' + str(data.get("apiStoreId")) + '/approval/' + str(data.get("status")) + '/update',
        headers=get_headers(), data=json.dumps(data))
    if update_resp.status_code == 200:
        resp = jsonify({'message': u'审批状态更新成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'审批状态更新失败！'})
        resp.status_code = 400
        return resp


@app.route('/api/apis/delete/<apiStoreId>', methods=['POST'])
@login_required
@cross_origin()
def api_apis_delete(apiStoreId):
    delete_resp = requests.delete(API_URL + 'apis/' + str(apiStoreId) , headers=get_headers())
    if delete_resp.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp


@app.route('/api/credit/wallet', methods=['POST'])
@login_required
@cross_origin()
def credit_wallet():

    if session.get('payment_flag') == False:
        resp = jsonify({'message': u'警告，您没有充值权限！'})
        resp.status_code = 400
        return resp

    request_data = json.loads(request.data)
    account_resp = requests.get(API_URL + 'accounts/company/' + str(request_data.get('userId')), headers=get_headers())
    account = {}
    if account_resp.status_code == 200:
        account = account_resp.json()

    wallet_resp = requests.get(API_URL + 'wallets/search?userId=' + str(account.get('id')), headers=get_headers())
    wallet = wallet_resp.json()[0]

    credit_request = {}
    credit_request['walletId'] = wallet.get('id')
    credit_request['lotType'] = request_data.get('lotType')
    credit_request['amount'] = request_data.get('amount')
    credit_request['reason'] = request_data.get('reason')
    credit_request['event'] = request_data.get('event')

    wallet_resp = requests.post(API_URL + 'wallets/credit', headers=get_headers(), data=json.dumps(credit_request))
    if wallet_resp.status_code == 200:
        wallet_transaction = wallet_resp.json()
        account = get_account_info(str(request_data.get('userId')))
        if (int(wallet_transaction.get('originalBalance')) > 0) and (int(wallet_transaction.get('currentBalance')) <= 0):
            account['forbiddenEnable'] = 'true'
            requests.put(API_URL + 'accounts/update', headers=get_headers(), data=json.dumps(account))
        elif (int(wallet_transaction.get('originalBalance')) <= 0) and (int(wallet_transaction.get('currentBalance')) > 0):
            account['forbiddenEnable'] = 'false'
            requests.put(API_URL + 'accounts/update', headers=get_headers(), data=json.dumps(account))
        resp = jsonify({'message': u'充值成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'充值失败！'})
        resp.status_code = 400
        return resp


@app.route('/api/debit/wallet', methods=['POST'])
@login_required
@cross_origin()
def debit_wallet():
    debit_request = json.loads(request.data)
    resp = requests.post('%s/debit' % API_URL, headers = get_headers(), data=json.dumps(debit_request))
    print resp.text


@app.route('/api/agents/register', methods=['POST'])
@login_required
@cross_origin()
def api_agents_register():

    data = json.loads(request.data)

    # check user name
    name_resp = requests.get(API_URL + 'agents/check-user-name/' + str(data.get("userName")),
                               headers=get_headers(), data=request.data, stream=True)

    if (name_resp.status_code == 400):
        resp = jsonify({'message': u'用户名【' + str(data.get('userName')) + '】已存在!'})
        resp.status_code = 400
        return resp

    # check mobile
    mobile_resp = requests.get(API_URL + 'agents/check-mobile/' + str(data.get("mobile")),
                               headers=get_headers(), data=request.data, stream=True)

    if (mobile_resp.status_code == 400):
        resp = jsonify({'message': u'手机号【' + str(data.get("mobile")) + '】已存在!'})
        resp.status_code = 400
        return resp

    register_resp = requests.post(API_URL + 'agents/register', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'添加成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'添加失败！请重新添加！'})
        resp.status_code = 400
        return resp


@app.route('/api/agents/search-page', methods=['GET'])
@login_required
@cross_origin()
def api_agents_search_page():
    agents = []
    page_no = "1" if request.args.get('pageNo') is None else request.args.get('pageNo')
    page_size = "15" if request.args.get('pageSize') is None else request.args.get('pageSize')
    keywords = "" if request.args.get('keywords') is None else request.args.get('keywords')
    select_response = requests.get(API_URL + 'agents/search-page?pageNo=' + page_no + '&pageSize=' + page_size + '&keywords=' + keywords, headers=get_headers())
    if select_response.status_code == 200:
        agents = select_response.json()
    return Response(json.dumps(agents))


@app.errorhandler(403)
def access_denied(e):
    return render_template('403.html'), 403


@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404


@app.errorhandler(500)
def server_error(e):
    return render_template('500.html'), 500


def create_session(employee):
    session['employee_id'] = employee.get('id')
    session['user_type'] = u'EMPLOYEE_USER'
    session['employee_name'] = employee.get('userName')
    session['real_name'] = employee.get('realName')
    if employee.get('userName') == 'liyanmei':
        session['payment_flag'] = True
    else:
        session['payment_flag'] = False

def get_login_info():
    loginInfo = {}
    loginInfo['userType'] = session.get('user_type')
    loginInfo['userName'] = session.get('employee_name')
    loginInfo['realName'] = session.get('real_name')
    return loginInfo

def get_account_info(user_id):
    account = {}
    account_response = requests.get(API_URL + 'accounts/' + str(user_id), headers=get_headers())
    if account_response.status_code == 200:
        account = account_response.json()
    return account

# md5加密
def md5(input):
    m = hashlib.md5()
    m.update(input)
    return m.hexdigest()


def utc2local(utc_st):
    utc_st = datetime.datetime.strptime(utc_st, '%Y-%m-%dT%H:%M:%SZ')
    now_stamp = time.time()
    local_time = datetime.datetime.fromtimestamp(now_stamp)
    utc_time = datetime.datetime.utcfromtimestamp(now_stamp)
    offset = local_time - utc_time
    local_st = utc_st + offset
    return local_st.strftime('%Y-%m-%d %H:%M')


def utcformate(utc_time):
    UTC_FORMAT = '%Y-%m-%dT%H:%M:%SZ'
    return datetime.datetime.strptime(utc_time, UTC_FORMAT)

# 生成exccel
def generate_excel(knowledges):
    now = int(time.mktime(datetime.datetime.now().timetuple()) * 1000)
    i = 0
    static_path = app.static_folder
    w = Workbook()  # 创建一个工作簿
    ws = w.add_sheet('export_staAccount')  # 创建一个工作表
    # ws.write(0, 0, '日期')  #
    # ws.write(0, 1, '单日新增注册用户(人)')
    # ws.write(0, 2, '注册用户总数(句)')
    # ws.write(0, 3, '单日有效问答(句)')
    # ws.write(0, 4, '单日无效效问答(句)')
    # ws.write(0, 5, '单日问答总量(句)')
    # ws.write(0, 6, '有效问答总量(句)')
    # ws.write(0, 7, '无效问答总量(句)')
    # ws.write(0, 8, '累计问答总量(句)')
    # ws.write(0, 9, '知识库新增容量(句)')
    # ws.write(0, 10, '知识库容量(句)')
    # ws.write(0, 11, '单日登录用户(人)')
    ws.write(0, 0, 'dataTime')  #
    ws.write(0, 1, 'dayAccountCount')
    ws.write(0, 2, 'allAccountCount')
    ws.write(0, 3, 'dayValidCount')
    ws.write(0, 4, 'dayInValidCount')
    ws.write(0, 5, 'dayQuestionCount')
    ws.write(0, 6, 'allValidCount')
    ws.write(0, 7, 'allInvalidCount')
    ws.write(0, 8, 'allQuestionCount')
    ws.write(0, 9, 'dayKnowledgeCount')
    ws.write(0, 10, 'allKnowledgeCount')
    ws.write(0, 11, 'dayLoginCount')
    knowledgeList = knowledges.json()
    filename = static_path + '/upload/staAccount' + str(now) + '.xls'
    for knowledge in knowledgeList:
        i = i + 1
        ws.write(i, 0, knowledge.get('dataTime'))
        ws.write(i, 1, knowledge.get('dayAccountCount'))
        ws.write(i, 2, knowledge.get('allAccountCount'))
        ws.write(i, 3, knowledge.get('dayValidCount'))
        ws.write(i, 4, knowledge.get('dayInValidCount'))
        ws.write(i, 5, knowledge.get('dayQuestionCount'))
        ws.write(i, 6, knowledge.get('allValidCount'))
        ws.write(i, 7, knowledge.get('allInvalidCount'))
        ws.write(i, 8, knowledge.get('allQuestionCount'))
        ws.write(i, 9, knowledge.get('dayKnowledgeCount'))
        ws.write(i, 10, knowledge.get('allKnowledgeCount'))
        ws.write(i, 11, knowledge.get('dayLoginCount'))
    w.save(filename)  # 保存
    return filename

def generate_knowledge_excel(knowledges):
    now = int(time.mktime(datetime.datetime.now().timetuple()) * 1000)
    i = 0
    static_path = app.static_folder
    w = Workbook()  # 创建一个工作簿
    ws = w.add_sheet('export_staAccount')  # 创建一个工作表
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

@app.route('/api/wechat/search-all-count-time', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def chat_sta_uuid_all_username_type():
    type = "" if request.args.get('type') is None else request.args.get('type')
    starttime = "" if request.args.get('starttime') is None else request.args.get('starttime')
    endtime = "" if request.args.get('endtime') is None else request.args.get('endtime')
    chat_sta_uuid_resp = requests.get(API_URL + 'wechat/search-all-count-time?type='+type+"&starttime="+starttime+"&endtime="+endtime, headers=get_headers())
    chat_sta = chat_sta_uuid_resp.json()
    return Response(json.dumps(chat_sta))

@app.route('/api/wechat/sta_wechat_chat_log', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def sta_wechat_chat_log():
    chat_sta_uuid_resp = requests.get(API_URL + 'wechat/sta_wechat_chat_log', headers=get_headers())
    chat_sta = chat_sta_uuid_resp.json()
    return Response(json.dumps(chat_sta))

@app.route('/api/accountLog/search', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def account_log_search():
    account_log_resp = requests.get(API_URL + 'accountLog/search', headers=get_headers())
    account_log = account_log_resp.json()
    return Response(json.dumps(account_log))
# 新增知识
@app.route('/api/knowledge/search-knowledge-num/<type>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def search_knowledge_num(type):
    knolwdge_viewses = requests.get(API_URL + 'knowledge/search-knowledge-num/'+type, headers=get_headers())
    knolwdge_views = knolwdge_viewses.json()
    return Response(json.dumps(knolwdge_views))

# 问答用户数
@app.route('/api/chat/record/search-question-answer-user/<type>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def search_question_answer_user_num(type):
    user_viewses = requests.get(API_URL + 'chat/record/search-question-answer-user/'+type, headers=get_headers())
    user_views = user_viewses.json()
    return Response(json.dumps(user_views))

# 问答量统计
@app.route('/api/chat/record/sta/search-question-answer-count/<type>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def search_question_answer_count(type):
    question_answer_count_viewses = requests.get(API_URL + 'chat/record/sta/search-question-answer-count/'+type, headers=get_headers())
    question_answer_count_views = question_answer_count_viewses.json()
    return Response(json.dumps(question_answer_count_views))

# 登录用户统计
@app.route('/api/accounts/search-login-user/<type>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def search_account_login_count(type):
    login_user_viewses = requests.get(API_URL + 'accounts/search-login-user/'+type, headers=get_headers())
    login_user_views = login_user_viewses.json()
    return Response(json.dumps(login_user_views))

# 新增注册用户
@app.route('/api/accounts/search-user/<type>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def search_account_count(type):
    user_viewses = requests.get(API_URL + 'accounts/search-type/'+type, headers=get_headers())
    user_views = user_viewses.json()
    return Response(json.dumps(user_views))

@app.route('/api/accounts/update-admin', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def account_update():
    request_data = json.loads(request.data)
    accounts_resp = requests.put(API_URL + 'accounts/update-admin', headers=get_headers(), data=json.dumps(request_data))
    if accounts_resp.status_code == 200:
        resp = jsonify({'message': u'修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'修改失败！'})
        resp.status_code = 400
        return resp
# -------------------------------------------------词库管理   begin---------------------------------------------------------------------------

# -------------------近义词替换   begin-----------------
@app.route('/api/lianxiangreplaceword/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def lianxiang_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    lianxiang_resp = requests.get(API_URL + 'lianxiangreplaceword/search-page?page_no='+page_no+"&keyword="+keyword, headers=get_headers())
    lianxiang = lianxiang_resp.json()
    return Response(json.dumps(lianxiang))

@app.route('/api/lianxiangreplaceword/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def lianxiang_get(id):
    lianxiang_resp = requests.get(API_URL + 'lianxiangreplaceword/'+id, headers=get_headers())
    lianxiang = lianxiang_resp.json()
    return Response(json.dumps(lianxiang))

@app.route('/api/lianxiangreplaceword/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def lianxiangreplaceword_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'lianxiangreplaceword/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'近义词替换添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/lianxiangreplaceword/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def lianxiangreplaceword_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'lianxiangreplaceword/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'近义词替换修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'近义词替换修改失败！'})
        resp.status_code = 400
        return resp

# 根据id删除近义词
@app.route('/api/lianxiangreplaceword/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def lianxiangreplaceword_delete(id):
    save_response = requests.put(API_URL + 'lianxiangreplaceword/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------近义词替换   end-----------------

# -------------------口语处理   begin-----------------
@app.route('/api/oraltreatment/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def oraltreatment_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    oraltreatment_resp = requests.get(API_URL + 'oraltreatment/search-page?page_no='+page_no+"&keyword="+keyword, headers=get_headers())
    oraltreatment = oraltreatment_resp.json()
    return Response(json.dumps(oraltreatment))

@app.route('/api/oraltreatment/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def oraltreatment_get(id):
    oraltreatment_resp = requests.get(API_URL + 'oraltreatment/'+id, headers=get_headers())
    oraltreatment = oraltreatment_resp.json()
    return Response(json.dumps(oraltreatment))

@app.route('/api/oraltreatment/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def oraltreatment_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'oraltreatment/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'口语处理添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/oraltreatment/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def oraltreatment_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'oraltreatment/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'口语处理修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'口语处理修改失败！'})
        resp.status_code = 400
        return resp

@app.route('/api/oraltreatment/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def oraltreatment_delete(id):
    save_response = requests.put(API_URL + 'oraltreatment/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------口语处理   end-----------------

# -------------------绝对替换   begin---------------
@app.route('/api/replaceword/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def replaceword_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    replaceword_resp = requests.get(API_URL + 'replaceword/search-page?page_no='+page_no+"&keyword="+keyword, headers=get_headers())
    replaceword = replaceword_resp.json()
    return Response(json.dumps(replaceword))

@app.route('/api/replaceword/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def replaceword_get(id):
    replaceword_resp = requests.get(API_URL + 'replaceword/'+id, headers=get_headers())
    replaceword = replaceword_resp.json()
    return Response(json.dumps(replaceword))

@app.route('/api/replaceword/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def replaceword_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'replaceword/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'绝对替换添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/replaceword/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def replaceword_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'replaceword/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'绝对替换修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'绝对替换修改失败！'})
        resp.status_code = 400
        return resp

@app.route('/api/replaceword/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def replaceword_delete(id):
    save_response = requests.put(API_URL + 'replaceword/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp

# 根据id批量删除知识
@app.route('/api/replaceword/batchdelete', methods=['POST'])
@login_required
@cross_origin()
def api_replaceword_batch_delete():
    request_data = json.loads(request.data)
    save_response = requests.put(API_URL + 'replaceword/batchdelete', headers=get_headers(),
                                 data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------绝对替换   end-----------------

# -------------------同义词管理 begin---------------
@app.route('/api/synonym/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def synonym_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    synonym_resp = requests.get(API_URL + 'synonym/search-page?page_no='+page_no+"&keyword="+keyword, headers=get_headers())
    synonym = synonym_resp.json()
    return Response(json.dumps(synonym))

@app.route('/api/synonym/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def synonym_get(id):
    synonym_resp = requests.get(API_URL + 'synonym/'+id, headers=get_headers())
    synonym = synonym_resp.json()
    return Response(json.dumps(synonym))

@app.route('/api/synonym/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def synonym_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'synonym/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'同义词添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/synonym/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def synonym_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'synonym/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'同义词修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'同义词修改失败！'})
        resp.status_code = 400
        return resp

@app.route('/api/synonym/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def synonym_delete(id):
    save_response = requests.put(API_URL + 'synonym/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------同义词管理 end-----------------

# -------------------原生词库管理 begin---------------
@app.route('/api/wordbase/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def wordbase_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    type = "1" if request.args.get('type') is None else request.args.get('type')
    wordbase_resp = requests.get(API_URL + 'wordbase/search-page?page_no='+page_no+"&keyword="+keyword+"&type="+type, headers=get_headers())
    wordbase = wordbase_resp.json()
    return Response(json.dumps(wordbase))

@app.route('/api/wordbase/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def wordbase_get(id):
    wordbase_resp = requests.get(API_URL + 'wordbase/'+id, headers=get_headers())
    wordbase = wordbase_resp.json()
    return Response(json.dumps(wordbase))

@app.route('/api/wordbase/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def wordbase_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'wordbase/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'词库添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/wordbase/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def wordbase_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'wordbase/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'词库修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'词库修改失败！'})
        resp.status_code = 400
        return resp

@app.route('/api/wordbase/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def wordbase_delete(id):
    save_response = requests.put(API_URL + 'wordbase/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp

# 根据id批量删除知识
@app.route('/api/wordbase/batchdelete', methods=['POST'])
@login_required
@cross_origin()
def api_wordbase_batch_delete():
    request_data = json.loads(request.data)
    save_response = requests.put(API_URL + 'wordbase/batchdelete', headers=get_headers(),
                                 data=json.dumps(request_data))
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------原生词库管理 end-----------------

# -------------------新词管理 begin-----------------
@app.route('/api/thesaurus/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def thesaurus_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    thesaurus_resp = requests.get(API_URL + 'thesaurus/search-page?page_no='+page_no+"&keyword="+keyword, headers=get_headers())
    thesaurus = thesaurus_resp.json()
    return Response(json.dumps(thesaurus))

@app.route('/api/thesaurus/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def thesaurus_get(id):
    thesaurus_resp = requests.get(API_URL + 'thesaurus/'+id, headers=get_headers())
    thesaurus = thesaurus_resp.json()
    return Response(json.dumps(thesaurus))

@app.route('/api/thesaurus/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def thesaurus_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'thesaurus/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'新词添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/thesaurus/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def thesaurus_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'thesaurus/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'新词修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'新词修改失败！'})
        resp.status_code = 400
        return resp

@app.route('/api/thesaurus/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def thesaurus_delete(id):
    save_response = requests.put(API_URL + 'thesaurus/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------新词管理   end-----------------

# -------------------敏感词   begin-----------------
@app.route('/api/sensitiveword/search-page', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def sensitiveword_search_page():
    page_no = "" if request.args.get('pageNo') is None else request.args.get('pageNo')
    keyword = "" if request.args.get('keyword') is None else request.args.get('keyword')
    sensitiveword_resp = requests.get(API_URL + 'sensitiveword/search-page?page_no='+page_no+"&keyword="+keyword, headers=get_headers())
    sensitiveword = sensitiveword_resp.json()
    return Response(json.dumps(sensitiveword))

@app.route('/api/sensitiveword/<id>', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def sensitiveword_get(id):
    sensitiveword_resp = requests.get(API_URL + 'sensitiveword/'+id, headers=get_headers())
    sensitiveword = sensitiveword_resp.json()
    return Response(json.dumps(sensitiveword))

@app.route('/api/sensitiveword/create', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def sensitiveword_create():
    data = json.loads(request.data)
    register_resp = requests.post(API_URL+'sensitiveword/create', headers=get_headers(), data=json.dumps(data))
    if register_resp.status_code == 200:
        resp = jsonify({'message': u'敏感词添加成功！'})
        resp.status_code = 200
        return resp
    else:
        message = u'添加失败！请重新添加！' if register_resp.json().get('message') is None else register_resp.json().get('message')
        resp = jsonify({'message': message})
        resp.status_code = 400
        return resp

@app.route('/api/sensitiveword/update', methods=['POST'])
@login_required  # 请求来源
@cross_origin()  # 转发
def sensitiveword_update():
    request_data = json.loads(request.data)
    update_response = requests.put(API_URL + 'sensitiveword/update', headers=get_headers(), data=json.dumps(request_data))
    if update_response.status_code == 200:
        resp = jsonify({'message': u'敏感词修改成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'敏感词修改失败！'})
        resp.status_code = 400
        return resp

@app.route('/api/sensitiveword/delete/<id>', methods=['POST'])
@login_required
@cross_origin()
def sensitiveword_delete(id):
    save_response = requests.put(API_URL + 'sensitiveword/delete/' + id, headers=get_headers())
    if save_response.status_code == 200:
        resp = jsonify({'message': u'删除成功！'})
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': u'删除失败！'})
        resp.status_code = 400
        return resp
# -------------------敏感词   end-------------------
# -------------------语义理解api   begin-----------------
@app.route('/api/yuyi/search', methods=['GET'])
@login_required  # 请求来源
@cross_origin()  # 转发
def yuyi_search_():
    # options.setShuru(nullIfEmpty(params.getFirst("shuru")));
    # options.setGjz(nullIfEmpty(params.getFirst("gjz")));
    # options.setXiangshi1(nullIfEmpty(params.getFirst("xiangshi1")));
    # options.setXiangshi2(nullIfEmpty(params.getFirst("xiangshi2")));
    # options.setBeforegjz(nullIfEmpty(params.getFirst("beforegjz")));
    # options.setScene(nullIfEmpty(params.getFirst("scene")));
    shuru = "" if request.args.get('shuru') is None else request.args.get('shuru')
    gjz = "" if request.args.get('gjz') is None else request.args.get('gjz')
    xiangshi1 = "" if request.args.get('xiangshi1') is None else request.args.get('xiangshi1')
    xiangshi2 = "" if request.args.get('xiangshi2') is None else request.args.get('xiangshi2')
    beforegjz = "" if request.args.get('beforegjz') is None else request.args.get('beforegjz')
    scene = "" if request.args.get('scene') is None else request.args.get('scene')
    yuyi_resp = requests.get(API_URL + 'yuyi/search?shuru='+shuru+"&gjz="+gjz+"&xiangshi1="+xiangshi1+"&xiangshi2="+xiangshi2+"&beforegjz="+beforegjz+"&scene="+scene, headers=get_headers())
    yuyi = yuyi_resp.json()
    return Response(json.dumps(yuyi))
# -------------------语义理解api   end-------------------
# -------------------------------------------------词库管理   end-----------------------------------------------------------------------------


def main1():
    app.run(host='0.0.0.0', port=5100, threaded=True, debug=True)


def main2():
    http_server = HTTPServer(WSGIContainer(app))
    http_server.listen(5100)
    print '-------- successful startup -------'
    IOLoop.instance().start()


app.jinja_env.globals['_suffix_'] = '?v=' + str(time.time())
app.secret_key = APP_KEY
app.config['SESSION_TYPE'] = 'filesystem'
app.config['APIURL'] = '/api'

if __name__ == "__main__":
    main2()
# -*-coding: utf-8-*-

import json
import base64
import sys
import threading

from flask import session

from settings import APP_KEY


reload(sys)
sys.setdefaultencoding("utf-8")

THREAD_LOCAL = threading.local()

def authenticated(r):
    if session.get('user_id'):
        return True
    return False


def get_headers():
    user_type = session.get('user_type')

    headers = {
        'Content-Type': 'application/json',
        'X-APP-KEY': APP_KEY
    }

    # logined
    if user_type:
        headers['X-USER-TYPE'] = user_type
        headers['X-USER-ID'] = session.get('user_id')

        name = session.get('user_name')
        if name is not None:
            headers['X-USER-NAME-BASE64'] = base64.b64encode(name)

    if THREAD_LOCAL.referrer:
        headers['X-REFERRER'] = THREAD_LOCAL.referrer

    return headers


def get_system_headers():
    return {
        'Content-Type': 'application/json',
        'X-APP-KEY': APP_KEY,
        'X-USER-TYPE': 'SYSTEM'
    }


# utils
class Object:
    """
    Empty class. We can add arbitrary attribute to it.
    """

    def __init__(self, **entries):
        self.__dict__.update(entries)

    def __getitem__(self, item):
        return self.__dict__[item]

    def __getattr__(self, item):
        return None

    def __str__(self):
        return json.dumps(self.__dict__, indent=4, ensure_ascii=False)


def _json_object_hook(d):
    return Object(**d)


def json2obj(data):
    return json.loads(data, object_hook=_json_object_hook)




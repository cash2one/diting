# -*-coding: utf-8-*-
import ssl
import urllib2
from random import Random

import qiniu
from qiniu import BucketManager
from flask import jsonify

ACCESS_KEY = "uitmSQ6vcOJzagNSf_O1r3Hgc14EIWSLwoaGA8GW"
SECRET_KEY = "f9gzwmMZo73VtvsvhTVAShw87zFjezU2TPWK9XAw"
BUCKET_NAME = "diting-picture"
BUCKET_DOMAIN = "http://diting-picture.pingxingren.cn"

# ACCESS_KEY = "e9K7segq4ycNV9U-fJ-NzkiXQrmGsg1l_5VlM0JE"
# SECRET_KEY = "6IgZBxXr455PqtxE32BV3lLbmCBC3sAiBHFP0L7z"
# BUCKET_NAME = "mofanghr"
# BUCKET_DOMAIN = "http://7xio5n.com1.z0.glb.clouddn.com"

ssl._create_default_https_context = ssl._create_unverified_context
# urllib2.urlopen("http://ok0imqo7o.bkt.clouddn.com").read()
#解析结果
def parseRet(retData, respInfo):
    if retData != None:
        print("Upload file success!")
        return BUCKET_DOMAIN+"/"+retData["key"]
    else:
        print("Upload file failed!")
        print("Error: " + respInfo.text_body)
        return None

#根据路径上传文件
def upload_file(filePath,key):
    #生成上传凭证
    print "生成上传凭证"
    auth = qiniu.Auth(ACCESS_KEY, SECRET_KEY)
    upToken = auth.upload_token(BUCKET_NAME, key)
    #上传文件
    print "上传文件"
    retData, respInfo = qiniu.put_file(upToken, key, filePath)
    #解析结果
    print "解析结果"
    return parseRet(retData, respInfo)

#上传二进制文件
def upload_data(data,key):
    auth = qiniu.Auth(ACCESS_KEY, SECRET_KEY)
    upToken = auth.upload_token(BUCKET_NAME, key)
    retData, respInfo = qiniu.put_data(upToken, key, data, mime_type="application/octet-stream", check_crc=True)
    #解析结果
    return parseRet(retData, respInfo)

# 删除文件
def del_file(key):
    auth = qiniu.Auth(ACCESS_KEY, SECRET_KEY)
    bucket = BucketManager(auth)
    retData, respInfo = bucket.delete(BUCKET_NAME, key)
    print respInfo
    if retData != None:
        resp = jsonify({'message': u'删除成功'})
        resp.status_code = 200
    else:
        resp = jsonify({'message': u'删除失败'})
        resp.status_code = 400
def random_str(randomlength):
    str = ''
    chars = 'AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789'
    length = len(chars) - 1
    random = Random()
    for i in range(randomlength):
        str+=chars[random.randint(0, length)]
    return str
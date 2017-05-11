import sys
reload(sys)
sys.setdefaultencoding("utf-8")

ACL = {
    'GET users': ['DITING_USER'],
}

def check_perm(method, url, roles):
    if '/' in url:
        url = url[0: url.index('/')]

    target_api = method + ' ' + url

    # private api
    if not ACL.has_key(target_api):
        return False

    # public api
    if ACL[target_api] == None:
        return True

    return set(ACL[target_api]) & set(roles)

    

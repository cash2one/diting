import sys
reload(sys)
sys.setdefaultencoding("utf-8")

ACL = {
    'GET auto-suggest': ['ADMIN', 'SALES', 'PM', 'HR', 'MARKETING', 'DM', 'MM'],
}

def check_perm(method, url, roles):
    if '/' in url:
        url = url[0: url.index('/')]

    target_api = method + ' ' + url
    print 'key->>'+target_api
    if not ACL.has_key(target_api):
        return False

    return set(ACL[target_api]) & set(roles)

    

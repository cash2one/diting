import os

flask_env = os.getenv('FLASK_ENV')
print 'FLASK_ENV: %s' % flask_env

if flask_env == 'PROD':
    print 'Loading config: PROD'
    from settings.prod import *
    
elif flask_env == 'PROD-LEGACY':
    print 'Loading config: PROD-LEGACY'
    from settings.prod_legacy import *
    
elif flask_env == 'TEST':
    print 'Loading config: TEST'
    from settings.test import *

else:
    print 'Loading config: DEV'
    from settings.dev import *

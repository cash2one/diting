from flask import Flask
import settings
import time
app = Flask(__name__)
app.config.from_object(settings)

from views.web_antusheng_ui import web_antusheng_ui
app.register_blueprint(web_antusheng_ui)

app.secret_key = settings.APP_KEY

app.jinja_env.globals['_suffix_'] = '?v=' + str(int(time.time())/60)


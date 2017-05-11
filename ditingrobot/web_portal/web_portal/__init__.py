from flask import Flask
import settings
import time
app = Flask(__name__)
app.config.from_object(settings)

from views.web_ui import web_ui
from views.wechat_ui import wechat_ui
from views.agent_ui import agent_ui
from views.segmentation_ui import segmentation_ui
app.register_blueprint(web_ui)
app.register_blueprint(wechat_ui, url_prefix='/wx')
app.register_blueprint(agent_ui, url_prefix='/dl')
app.register_blueprint(segmentation_ui, url_prefix='/seg')

app.secret_key = settings.APP_KEY

app.jinja_env.globals['_suffix_'] = '?v=' + str(int(time.time())/60)


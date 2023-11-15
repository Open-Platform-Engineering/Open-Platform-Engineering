# app.py
from flask import Flask, render_template, redirect, request, Response,url_for
# from flask_assets import Bundle, Environment

from web_ui_htmx_flask.todo import todos
import requests
import os

API_HOST = os.environ.get('API_HOST', "http://localhost:8080")
app = Flask(__name__)


# assets = Environment(app)
# css = Bundle("src/main.css", output="dist/main.css")
# js = Bundle("src/*.js", output="dist/main.js")
#
# assets.register("css", css)
# assets.register("js", js) # new
# css.build()
# js.build() # new


@app.route("/")
def homepage():
    return render_template("index.html")


@app.route("/sign-up/email/vlidation/<email>", methods=["GET"])
def emailValidation(email):
    return render_template("email-validation.html", email=email)


@app.route("/sign-up", methods=["GET"])
def signUpGet():
    return render_template("sign-up.html")


@app.route("/sign-up", methods=["POST"])
def signUpPost():
    email = request.form['email']
    password = request.form['password']
    res = requests.request(
        method="post",
        url=f'{API_HOST}/v1/sign-up',
        headers={'Content-type': 'application/json'},
        json={"email":email,"password":password},
        cookies=request.cookies,
        allow_redirects=False,
    )
    app.logger.info('%s logged in successfully', res.status_code)
    if res.status_code == 200:
        resp = Response(headers={"HX-Redirect":url_for("emailValidation", email=email)},status=200,  mimetype="text/plain")
        return resp
        # return redirect(url_for("emailValidation", email=email))
        # return redirect("/sign-up/email/vlidation/"+email, code=302)
    else:
        return render_template("sign-up-error.html")


if __name__ == "__main__":
    app.run(debug=True)
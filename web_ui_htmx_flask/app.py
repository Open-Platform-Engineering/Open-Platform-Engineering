# app.py
from flask import Flask, render_template, redirect, request, Response,url_for
# from flask_assets import Bundle, Environment

import requests
import os
from web_ui_htmx_flask.zoneid import zoneid_list

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
    token = request.cookies.get('token')
    email = request.cookies.get('email')
    return render_template("index.html", email=email)


@app.route("/services", methods=["GET"])
def services():
    email = request.cookies.get('email')
    
    return render_template("/services/index.html", email=email)

@app.route("/schedules", methods=["GET"])
def schedules():
    email = request.cookies.get('email')
    res = requests.request(
        method="get",
        url=f'{API_HOST}/v1/shedules',
        headers={'Content-type': 'application/json','token': request.cookies['token']},
        allow_redirects=False,
    )
    if res.status_code == 200:
        return render_template("/schedules/index.html", email=email, pagination=res.json())
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        return render_template("/schedules/index.html", email=email, pagination = None)

@app.route("/schedules/create", methods=["GET"])
def schedulesCreate():
    email = request.cookies.get('email')
    return render_template("/schedules/create.html", email=email, zoneid_list=zoneid_list)

@app.route("/teams/search", methods=["POST"])
def teamsSearch():
    email = request.cookies.get('email')
    q = request.form['q']
    app.logger.info('team search, key:%s', q)
    res = requests.request(
        method="get",
        url=f'{API_HOST}/v1/teams/search',
        params={"q":q},
        headers={'Content-type': 'application/json','token': request.cookies['token']},
        allow_redirects=False,
    )
    app.logger.info('team search, status:%d', res.status_code)
    if res.status_code == 200:
        return render_template("/schedules/teamsSearch.html", email=email, pagination=res.json())
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        return render_template("/teams/index.html", email=email)

@app.route("/teams", methods=["GET"])
def teams():
    email = request.cookies.get('email')
    res = requests.request(
        method="get",
        url=f'{API_HOST}/v1/teams',
        headers={'Content-type': 'application/json','token': request.cookies['token']},
        allow_redirects=False,
    )
    app.logger.info('team list, status:%d', res.status_code)
    if res.status_code == 200:
        return render_template("/teams/index.html", email=email, pagination=res.json())
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        return render_template("/teams/index.html", email=email)

@app.route("/teams/create", methods=["GET"])
def teamsCreate():
    email = request.cookies.get('email')
    return render_template("/teams/create.html", email=email)

@app.route("/teams/create", methods=["POST"])
def teamsCreatePost():
    name = request.form['name']
    res = requests.request(
        method="post",
        url=f'{API_HOST}/v1/team',
        headers={'Content-type': 'application/json','token': request.cookies['token']},
        json={"name":name},
        allow_redirects=False,
    )
    if res.status_code == 200:
        app.logger.info('team %s created', name)
        resp = Response(headers={"HX-Redirect":url_for("teams")},status=200,  mimetype="text/plain")
        return resp
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        return Response(body="team create error",status=res.status_code,  mimetype="text/plain")


@app.route("/sign-up/email/vlidation/<email>", methods=["GET"])
def emailValidationGet(email):
    return render_template("email-validation.html", email=email)


@app.route("/sign/up/email/validate", methods=["POST"])
def emailValidationPost():
    email = request.form['email']
    code = request.form['code']
    res = requests.request(
        method="post",
        url=f'{API_HOST}/v1/sign/up/email/validate',
        headers={'Content-type': 'application/json'},
        json={"email":email,"code":code},
        allow_redirects=False,
    )
    app.logger.info('%s logged in successfully', res.status_code)
    if res.status_code == 200:
        resp = Response(headers={"HX-Redirect":url_for("signInGet")},status=200,  mimetype="text/plain")
        return resp
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        resp = Response(body="code is incorrect",status=401,  mimetype="text/plain")
        return resp

@app.route("/sign-up", methods=["GET"])
def signUpGet():
    return render_template("sign-up.html",email=None)

@app.route("/sign-in", methods = ["GET"])
def signInGet():
    return render_template("sign-in.html",email=None)

@app.route("/sign-in", methods = ["POST"])
def signInPost():
    email = request.form['email']
    password = request.form['password']
    res = requests.request(
        method="post",
        url=f'{API_HOST}/v1/sign-in',
        headers={'Content-type': 'application/json'},
        json={"email":email, "password":password},
        cookies=request.cookies,
        allow_redirects=False,
    )
    app.logger.info('%s logged status code %s ', email, res.status_code)
    if res.status_code == 200:
        resp = Response(headers={"HX-Redirect":url_for("homepage")},status=200,  mimetype="text/plain")
        resp.set_cookie('token', res.headers['token'])
        resp.set_cookie('email', res.headers['email'])
        return resp
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        return render_template("sign-up-error.html")



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
    app.logger.info('%s sign up successfully', res.status_code)
    if res.status_code == 200:
        resp = Response(headers={"HX-Redirect":url_for("emailValidationGet", email=email)},status=200,  mimetype="text/plain")
        return resp
        # return redirect(url_for("emailValidation", email=email))
        # return redirect("/sign-up/email/vlidation/"+email, code=302)
    elif res.status_code == 401:
        return redirect("/sign-in", code=302)
    else:
        return render_template("sign-up-error.html")


if __name__ == "__main__":
    app.run(debug=True)

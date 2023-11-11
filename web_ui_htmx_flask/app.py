# app.py

from flask import Flask, render_template, request
# from flask_assets import Bundle, Environment

from web_ui_htmx_flask.todo import todos


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

@app.route("/sign-up")
def signUp():
    return render_template("sign-up.html")
@app.route("/search", methods=["POST"])
def search_todo():
    search_term = request.form.get("search")

    if not len(search_term):
        return render_template("todo.html", todos=[])

    res_todos = []
    for todo in todos:
        if search_term in todo["title"]:
            res_todos.append(todo)

    return render_template("todo.html", todos=res_todos)


if __name__ == "__main__":
    app.run(debug=True)

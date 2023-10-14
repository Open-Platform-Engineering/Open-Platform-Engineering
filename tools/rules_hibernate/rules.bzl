

def _hibernate_ddl_impl(ctx):
  output = ctx.actions.declare_file(ctx.label.name + ".sql")
  entity_lib_path = ctx.file.entity_lib.path
  args = ctx.actions.args()

  jar_delimiter = ":"
  if ctx.attr.is_windows:
      jar_delimiter = ";"

  class_path = jar_delimiter.join([f.path for f in ctx.attr.entity_lib[JavaInfo].full_compile_jars.to_list()]
    + [f.path for f in ctx.attr.entity_lib[JavaInfo].transitive_compile_time_jars.to_list()])

  args.add("--main_advice_classpath=" + class_path)
  args.add('dialect', ctx.attr.dialect)
  args.add('format', ctx.attr.format)
  args.add('delimiter', ctx.attr.delimiter)
  args.add('outputFile', output.path)
  args.add('haltOnError', ctx.attr.haltOnError)
  args.add('packages', ",".join([package for package in ctx.attr.packages]))

  runfiles = ctx.runfiles(files = [ctx.file.entity_lib]+ [output])

  ctx.actions.run(
        inputs = depset(
             direct = [ctx.file.entity_lib],
             transitive = [ctx.attr.entity_lib[JavaInfo].transitive_compile_time_jars]
         ),
        outputs = [output],
        arguments = [args],
        progress_message = "Generate sql from entity:{} to {}".format(entity_lib_path, output.path),
        executable = ctx.executable._executable,
        mnemonic = "HibernateSchemaDDL",
  )
  return [
      DefaultInfo(
        runfiles = runfiles,
        files = depset([output])
      )
  ]

_hibernate_ddl = rule(
  implementation = _hibernate_ddl_impl,
  attrs = {
    "is_windows": attr.bool(mandatory = True),
    "format": attr.bool(
      default = True,
    ),
    "haltOnError": attr.bool(
      default = True,
    ),
    "delimiter": attr.string(
      default = ";"
    ),
    "dialect": attr.string(
        mandatory = True,
    ),
    "packages":attr.string_list(
        mandatory = True,
    ),
    "entity_lib":attr.label(
      mandatory = True,
      allow_single_file = True,
    ),
    "_executable": attr.label(
      cfg="host",
      executable = True,
      default="//tools/rules_hibernate:SchemaGeneratorCommand"
    ),
  },
  fragments = ["java"],
)


def hibernate_ddl(name, **kwargs):
    _hibernate_ddl(
        name = name,
        is_windows = select({
            "@bazel_tools//src/conditions:windows": True,
            "//conditions:default": False,
        }),
        **kwargs
    )

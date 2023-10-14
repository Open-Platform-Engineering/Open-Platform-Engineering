
def _ebean_ddl_impl(ctx):

  output = ctx.actions.declare_file(ctx.label.name)
  args = ctx.actions.args()
#  print("Mmmmmmmmmm")
#  args.add("--jvm_flag=" + "-javaagent:" + ctx.attr.ebean_agent_str)
#  args.add("--jvm_flag="+"-javaagent:../../../../"+ ctx.file.ebean_agent.short_path )
#  args.add('--main_advice_classpath=' + entity_lib_path)
  args.add("--jvm_flag=" + "-javaagent:/Users/jack/codebase/everything-in-code-planet/bazel-out/darwin-fastbuild/bin/external/maven/v1/https/maven.aliyun.com/repository/public/io/ebean/ebean-agent/13.20.1/ebean-agent-13.20.1.jar")
  packages = ",".join([package for package in ctx.attr.packages])
  args.add('packages', packages)
  args.add("path", output.path)
  if(ctx.files.migration_sql):
    args.add("migration_sql", ",".join([m.path for m in ctx.files.migration_sql]))
  if(ctx.files.migration_model):
    args.add("migration_model", ",".join([m.path for m in ctx.files.migration_model]))


  args.add('transformArgs', ",".join([arg for arg in ctx.attr.transformArgs]))
  runfiles = ctx.runfiles(files = [ctx.file.entity_lib] + [ctx.file.ebean_agent]+ ctx.files.migration_sql + ctx.files.migration_model)
  ctx.actions.run(
        inputs = depset(
             direct = [ctx.file.entity_lib] + ctx.files.migration_sql + ctx.files.migration_model,
             transitive = [ctx.attr.entity_lib[JavaInfo].transitive_compile_time_jars]
         ),
        outputs = [output],
        arguments = [args],
        progress_message = "ebean ddl packages:{}".format(packages),
        executable = ctx.executable.executable,
        mnemonic = "EbeanDDL",
  )
  return [
      DefaultInfo(
        runfiles = runfiles,
        files = depset([output])
      )
  ]

_ebean_ddl = rule(
  implementation = _ebean_ddl_impl,
  attrs = {
    "entity_lib":attr.label(
      mandatory = True,
      allow_single_file = True,
    ),
    "packages":attr.string_list(
        mandatory = True,
    ),
    "ebean_agent":attr.label(
        allow_single_file = True,
    ),
    "migration_sql":attr.label(
        allow_files = [".sql"],
#        allow_empty = True,
    ),
    "migration_model":attr.label(
        allow_files = [".xml"],
#        allow_empty = True,
    ),
    "transformArgs":attr.string_list(),
    "executable": attr.label(
      cfg="host",
      executable = True,
      mandatory = True,
#      default="//tools/rules_ebean:DdlGeneratorCommand"
    ),
    "_java_toolchain": attr.label(
      default = "@bazel_tools//tools/jdk:current_java_toolchain",
    ),
    "_jdk": attr.label(
        default = Label("@bazel_tools//tools/jdk:current_java_runtime"),
        providers = [java_common.JavaRuntimeInfo],
    ),
  },
  fragments = ["java"],
  # # provides = [JavaInfo],
  # toolchains = [
  #       "@bazel_tools//tools/jdk:toolchain_type",
  # ],
)

def ebean_ddl(name, **kwargs):
#    native.genrule(
#        name=name + 'ebeanagent',
#        outs=['ebeanagent.jar'],
#        srcs=['@ebean_agent//jar'],
#        cmd='cp $(location @ebean_agent//jar) $@',
#    )

    _ebean_ddl(
        name = name,
        executable = "//tools/rules_ebean/src/main/java/internal:ebean_ddl",
        **kwargs
    )


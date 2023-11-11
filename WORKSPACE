workspace(name = "everything_in_code_planet")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive","http_file", "http_jar")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "new_git_repository","git_repository")



# skylib 记得放在最上面。因为先加载
http_archive(
    name = "bazel_skylib",
    sha256 = "b8a1527901774180afc798aeb28c4634bdccf19c4d98e7bdd1ce79d1fe9aaad7",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

# aspect bazel_lib start
http_archive(
    name = "aspect_bazel_lib",
    sha256 = "ce259cbac2e94a6dff01aff9455dcc844c8af141503b02a09c2642695b7b873e",
    strip_prefix = "bazel-lib-1.37.0",
    url = "https://github.com/aspect-build/bazel-lib/releases/download/v1.37.0/bazel-lib-v1.37.0.tar.gz",
)

load("@aspect_bazel_lib//lib:repositories.bzl", "aspect_bazel_lib_dependencies")
aspect_bazel_lib_dependencies()

# aspect bazel_lib end




### rules_cc start

http_archive(
    name = "rules_cc",
    urls = ["https://github.com/bazelbuild/rules_cc/releases/download/0.0.6/rules_cc-0.0.6.tar.gz"],
    sha256 = "3d9e271e2876ba42e114c9b9bc51454e379cbf0ec9ef9d40e2ae4cec61a31b40",
    strip_prefix = "rules_cc-0.0.6",
)
load("@rules_cc//cc:repositories.bzl", "rules_cc_dependencies", "rules_cc_toolchains")

rules_cc_dependencies()

rules_cc_toolchains()
### rules_cc end

http_archive(
    name = "io_bazel_rules_go",
    sha256 = "6dc2da7ab4cf5d7bfc7c949776b1b7c733f05e56edc4bcd9022bb249d2e2a996",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_go/releases/download/v0.39.1/rules_go-v0.39.1.zip",
        "https://github.com/bazelbuild/rules_go/releases/download/v0.39.1/rules_go-v0.39.1.zip",
    ],
)

http_archive(
    name = "bazel_gazelle",
    sha256 = "727f3e4edd96ea20c29e8c2ca9e8d2af724d8c7778e7923a854b2c80952bc405",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-gazelle/releases/download/v0.30.0/bazel-gazelle-v0.30.0.tar.gz",
        "https://github.com/bazelbuild/bazel-gazelle/releases/download/v0.30.0/bazel-gazelle-v0.30.0.tar.gz",
    ],
)


load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")
load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies", "go_repository")

############################################################
# Define your own dependencies here using go_repository.
# Else, dependencies declared by rules_go/gazelle will be used.
# The first declaration of an external repository "wins".
############################################################

go_rules_dependencies()

go_register_toolchains(version = "1.20.4")

gazelle_dependencies()



#http_archive(
#    name = "remotejdk11_linux",
#    build_file = "@bazel_tools//tools/jdk:jdk.BUILD",
#    sha256 = "6c79bfe8bb06c82b72ef2f293a14becef56b3078d298dc75fda4225cbb2d3d0c",
#    strip_prefix = "zulu11.43.55-ca-jdk11.0.9.1-linux_x64",
#    urls = [
#       "https://mirror.bazel.build/openjdk/azul-zulu11.43.55-ca-jdk11.0.9.1/zulu11.43.55-ca-jdk11.0.9.1-linux_x64.tar.gz",
#       "https://cdn.azul.com/zulu/bin/zulu11.43.55-ca-jdk11.0.9.1-linux_x64.tar.gz",
#    ],
#)
#http_archive(
#    name = "remote_java_tools_linux",
#    sha256 = "355c27c603e8fc64bb0e2d7f809741f42576d5f4540f9ce28fd55922085af639",
#    urls = [
#        "https://mirror.bazel.build/bazel_java_tools/releases/javac11/v10.5/java_tools_javac11_linux-v10.5.zip",
#        "https://github.com/bazelbuild/java_tools/releases/download/javac11_v10.5/java_tools_javac11_linux-v10.5.zip",
#    ],
#)

### jsonnet start

go_repository(
    name = "com_github_fatih_color",
#    build_external = "external",
    importpath = "github.com/fatih/color",
    sum = "h1:mRhaKNwANqRgUBGKmnI5ZxEk7QXmjQeCcuYFMX2bfcc=",
    version = "v1.12.0",
)

http_archive(
    name = "io_bazel_rules_jsonnet",
    sha256 = "d20270872ba8d4c108edecc9581e2bb7f320afab71f8caa2f6394b5202e8a2c3",
    strip_prefix = "rules_jsonnet-0.4.0",
    urls = ["https://github.com/bazelbuild/rules_jsonnet/archive/0.4.0.tar.gz"],
)


CPP_JSONNET_SHA256 = "af7c9c102daab64de39fe9e479acc7389b8dd2d0647c2f9c6abc9c429070b0b8"
CPP_JSONNET_GITHASH = "813c7412d1c7a42737724d011618d0fd7865bc69"

http_archive(
    name = "cpp_jsonnet",
    sha256 = CPP_JSONNET_SHA256,
    strip_prefix = "jsonnet-%s" % CPP_JSONNET_GITHASH,
    urls = ["https://github.com/google/jsonnet/archive/%s.tar.gz" % CPP_JSONNET_GITHASH],
)

go_repository(
        name = "com_github_davecgh_go_spew",
        importpath = "github.com/davecgh/go-spew",
        sum = "h1:vj9j/u1bqnvCEfJOwUhtlOARqs3+rkHYY13jYWTU97c=",
        version = "v1.1.1",
)



go_repository(
    name = "com_github_kr_pretty",
    importpath = "github.com/kr/pretty",
    sum = "h1:L/CwN0zerZDmRFUapSPitk6f+Q3+0za1rQkzVuMiMFI=",
    version = "v0.1.0",
)
go_repository(
    name = "com_github_kr_pty",
    importpath = "github.com/kr/pty",
    sum = "h1:VkoXIwSboBpnk99O/KFauAEILuNHv5DVFKZMBN/gUgw=",
    version = "v1.1.1",
)
go_repository(
    name = "com_github_kr_text",
    importpath = "github.com/kr/text",
    sum = "h1:45sCR5RtlFHMR4UwH9sdQ5TC8v0qDQCHnXt+kaKSTVE=",
    version = "v0.1.0",
)
go_repository(
    name = "com_github_mattn_go_colorable",
    build_external = "external",
    importpath = "github.com/mattn/go-colorable",
    sum = "h1:c1ghPdyEDarC70ftn0y+A/Ee++9zz8ljHG1b13eJ0s8=",
    version = "v0.1.8",
)
go_repository(
    name = "com_github_mattn_go_isatty",
    build_external = "external",
    importpath = "github.com/mattn/go-isatty",
    sum = "h1:wuysRhFDzyxgEmMf5xjvJ2M9dZoWAXNNr5LSBS7uHXY=",
    version = "v0.0.12",
)
go_repository(
    name = "com_github_pmezard_go_difflib",
    importpath = "github.com/pmezard/go-difflib",
    sum = "h1:4DBwDE0NGyQoBHbLQYPwSUPoCMWR5BEzIk/f1lZbAQM=",
    version = "v1.0.0",
)
go_repository(
    name = "com_github_sergi_go_diff",
    importpath = "github.com/sergi/go-diff",
    sum = "h1:we8PVUC3FE2uYfodKH/nBHMSetSfHDR6scGdBi+erh0=",
    version = "v1.1.0",
)
go_repository(
    name = "com_github_stretchr_objx",
    importpath = "github.com/stretchr/objx",
    sum = "h1:4G4v2dO3VZwixGIRoQ5Lfboy6nUhCyYzaqnIAPPhYs4=",
    version = "v0.1.0",
)
go_repository(
    name = "com_github_stretchr_testify",
    importpath = "github.com/stretchr/testify",
    sum = "h1:2E4SXV/wtOkTonXsotYi4li6zVWxYlZuYNCXe9XRJyk=",
    version = "v1.4.0",
)
go_repository(
    name = "in_gopkg_check_v1",
    importpath = "gopkg.in/check.v1",
    sum = "h1:YR8cESwS4TdDjEe65xsg0ogRM/Nc3DYOhEAlW+xobZo=",
    version = "v1.0.0-20190902080502-41f04d3bba15",
)
go_repository(
    name = "in_gopkg_yaml_v2",
    importpath = "gopkg.in/yaml.v2",
    sum = "h1:VUgggvou5XRW9mHwD/yXxIYSMtY0zoKQf/v226p2nyo=",
    version = "v2.2.7",
)
go_repository(
    name = "io_k8s_sigs_yaml",
    importpath = "sigs.k8s.io/yaml",
    sum = "h1:4A07+ZFc2wgJwo8YNlQpr1rVlgUDlxXHhPJciaPY5gs=",
    version = "v1.1.0",
)
go_repository(
    name = "org_golang_x_crypto",
    importpath = "golang.org/x/crypto",
    sum = "h1:LF6fAI+IutBocDJ2OT0Q1g8plpYljMZ4+lty+dsqw3g=",
    version = "v0.9.0",
)
go_repository(
    name = "org_golang_x_net",
    importpath = "golang.org/x/net",
    sum = "h1:X2//UzNDwYmtCLn7To6G58Wr6f5ahEAQgKNzv9Y951M=",
    version = "v0.10.0",
)

go_repository(
    name = "org_golang_x_sys",
    importpath = "golang.org/x/sys",
    sum = "h1:EBmGv8NaZBZTWvrbjNoL6HVt+IVy3QDQpJs7VRIw3tU=",
    version = "v0.8.0",
)
go_repository(
    name = "org_golang_x_term",
    importpath = "golang.org/x/term",
    sum = "h1:n5xxQn2i3PC0yLAbjTpNT85q/Kgzcr2gIoX9OrJUols=",
    version = "v0.8.0",
)
go_repository(
    name = "org_golang_x_text",
    importpath = "golang.org/x/text",
    sum = "h1:2sjJmO8cDvYveuX97RDLsxlyUxLl+GHoLxBiRdHllBE=",
    version = "v0.9.0",
)

http_archive(
    name = "jsonnet",
    sha256 = "85c240c4740f0c788c4d49f9c9c0942f5a2d1c2ae58b2c71068107bc80a3ced4",
    strip_prefix = "jsonnet-0.18.0",
    urls = [
        "https://github.com/google/jsonnet/archive/v0.18.0.tar.gz",
    ],
)

http_archive(
    name = "google_jsonnet_go",
    sha256 = "20fdb3599c2325fb11a63860e7580705590faf732abf47ed144203715bd03a70",
    strip_prefix = "go-jsonnet-0d78479d37eabd9451892dd02be2470145b4d4fa",
    urls = ["https://github.com/google/go-jsonnet/archive/0d78479d37eabd9451892dd02be2470145b4d4fa.tar.gz"],
)
### jsonnet end




## jvm
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "4.5"
RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

SPRING_BOOT_VERSION = "3.1.0"
SPRING_VERSION = "6.0.9"
HBERNATE_VERSION = "6.2.0.Final"
JACKSON_VERSION = "2.15.2"
EBEAN_VERSION = "13.23.2-jakarta"
SHIRO_VERSION = "1.12.0"
maven_install(
    artifacts = [
        # 日志
        "org.slf4j:slf4j-api:2.0.7",
        "ch.qos.logback:logback-classic:1.4.6",
        # 前端
        "org.springframework.boot:spring-boot-starter-thymeleaf:%s" % SPRING_BOOT_VERSION,
        # spring
        "org.springframework.boot:spring-boot-autoconfigure:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-configuration-processor:%s" % SPRING_BOOT_VERSION,
        "org.springframework.data:spring-data-jpa:%s" % "3.1.0",
        "org.springframework.boot:spring-boot-test-autoconfigure:%s" % SPRING_BOOT_VERSION,
        "javax.servlet:javax.servlet-api:%s" % "3.1.0",
        "org.springframework.boot:spring-boot-starter-test:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-validation:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-data-jpa:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-test:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-web:%s" % SPRING_BOOT_VERSION,
        "org.springframework:spring-webmvc:%s" % SPRING_VERSION,
        "org.springframework:spring-beans:%s" % SPRING_VERSION,
        "org.springframework:spring-context:%s" % SPRING_VERSION ,
        "org.springframework:spring-test:%s" % SPRING_VERSION,
        "org.springframework:spring-web:%s" % SPRING_VERSION,
        "org.springframework:spring-core:%s" % SPRING_VERSION,
        "org.springframework:spring-orm:%s" % SPRING_VERSION,
        "org.springframework:spring-tx:%s" % SPRING_VERSION,
        "jakarta.servlet:jakarta.servlet-api:6.0.0",
        'javax.annotation:javax.annotation-api:1.3.2',

        # security
        'org.apache.shiro:shiro-core:%s' % SHIRO_VERSION,
        'org.apache.shiro:shiro-web:%s' % SHIRO_VERSION,
        'org.apache.shiro:shiro-lang:%s' % SHIRO_VERSION,
        'org.apache.shiro:shiro-crypto-hash:%s' % SHIRO_VERSION,
        'org.apache.shiro:shiro-crypto-core:%s' % SHIRO_VERSION,
        'org.apache.shiro:shiro-spring-boot-web-starter:%s' % SHIRO_VERSION,
        'org.apache.shiro:shiro-spring:%s' % SHIRO_VERSION,
        'org.aspectj:aspectjweaver:1.9.20.1',

        # persistence
        "org.hibernate.validator:hibernate-validator:8.0.1.Final",
        "jakarta.validation:jakarta.validation-api:3.0.2",
        "javax.validation:validation-api:jar:2.0.0.Final",
        "jakarta.persistence:jakarta.persistence-api:3.1.0",
        "org.hibernate.orm:hibernate-core:%s" % HBERNATE_VERSION,
        "org.hibernate.orm:hibernate-ant:%s" % HBERNATE_VERSION,
        "org.hibernate.orm:hibernate-hikaricp:%s" % HBERNATE_VERSION,
        "com.zaxxer:HikariCP:5.0.1",

        # ebean
        'io.ebean:ebean-api:%s' % EBEAN_VERSION,
        'io.ebean:ebean-platform-all:%s' % EBEAN_VERSION,
        'io.ebean:ebean-ddl-generator:%s' % EBEAN_VERSION,
        'io.ebean:ebean-core:%s' % EBEAN_VERSION,
        'io.ebean:ebean-core-type:%s' % EBEAN_VERSION,
        'io.ebean:ebean-jackson-mapper:%s' % EBEAN_VERSION,
        'io.ebean:ebean-annotation:%s' % "8.4",
        'io.ebean:ebean-agent:%s' % "13.20.1",
        'io.ebean:ebean-migration:%s' % "13.9.0",
        'io.ebean:ebean-ddl-runner:%s' % '2.3',
        'io.ebean:ebean-datasource:%s' % "8.5",
        'io.ebean:ebean-postgres:%s' % EBEAN_VERSION,
        'io.ebean:ebean-platform-postgres:%s' % EBEAN_VERSION,

        # utils
        "org.reflections:reflections:0.10.2",
        "com.google.guava:guava:32.1.3-jre",
        "commons-cli:commons-cli:1.5.0",

        # json
        "com.fasterxml.jackson.core:jackson-core:%s" % JACKSON_VERSION,
        "com.fasterxml.jackson.core:jackson-databind:%s" % JACKSON_VERSION,
        'com.fasterxml.jackson.core:jackson-annotations:%s'  % JACKSON_VERSION,
        'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:%s'  % JACKSON_VERSION,
        # jdbc
        "org.postgresql:postgresql:42.6.0",
        # 单元测试
        "junit:junit:4.12",
        "org.hamcrest:hamcrest-core:1.3",
        "org.mockito:mockito-core:5.3.1",

        # 集成测试
        "org.testcontainers:testcontainers:1.19.0",
        "org.testcontainers:postgresql:1.19.0",
        # sql版本化
        "org.flywaydb:flyway-core:9.16.3",
        # observation
        'io.micrometer:micrometer-core:1.11.2',
        'io.micrometer:micrometer-registry-prometheus:1.11.2',
        'io.micrometer:micrometer-observation:1.11.2',

        # 间接依赖
        "com.github.docker-java:docker-java-api:3.3.0",

    ],
    excluded_artifacts = [
        "io.ebean:jakarta-persistence-api",
    ],
    fetch_sources = True,
    maven_install_json = "//:maven_install.json",
    repositories = [
#        "https://oss.sonatype.org/content/repositories/releases/",
        "https://maven.aliyun.com/repository/public",
#        "https://repo1.maven.org/maven2",
    ],
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()
# jvm end



## nodejs start
node_version = "16.14.2"
http_archive(
    name = "rules_nodejs",
    sha256 = "764a3b3757bb8c3c6a02ba3344731a3d71e558220adcb0cf7e43c9bba2c37ba8",
    urls = ["https://github.com/bazelbuild/rules_nodejs/releases/download/5.8.2/rules_nodejs-core-5.8.2.tar.gz"],
)
http_archive(
    name = "aspect_rules_js",
    sha256 = "bdbd6df52fc7963f55281fe0a140e21de8ec587ab711a8a2fff0715b6710a4f8",
    strip_prefix = "rules_js-1.32.0",
    url = "https://github.com/aspect-build/rules_js/releases/download/v1.32.0/rules_js-v1.32.0.tar.gz",
)

http_archive(
    name = "aspect_rules_webpack",
    sha256 = "21a85849d01eebbd0cb0a5c0120eb58e4d3275eda68565918e7c0d84e14d30d9",
    strip_prefix = "rules_webpack-0.13.0",
    url = "https://github.com/aspect-build/rules_webpack/releases/download/v0.13.0/rules_webpack-v0.13.0.tar.gz",
)

http_archive(
    name = "aspect_rules_ts",
    sha256 = "4c3f34fff9f96ffc9c26635d8235a32a23a6797324486c7d23c1dfa477e8b451",
    strip_prefix = "rules_ts-1.4.5",
    url = "https://github.com/aspect-build/rules_ts/releases/download/v1.4.5/rules_ts-v1.4.5.tar.gz",
)
http_archive(
    name = "aspect_rules_swc",
    sha256 = "b647c7c31feeb7f9330fff08b45f8afe7de674d3a9c89c712b8f9d1723d0c8f9",
    strip_prefix = "rules_swc-1.0.1",
    url = "https://github.com/aspect-build/rules_swc/releases/download/v1.0.1/rules_swc-v1.0.1.tar.gz",
)

http_archive(
    name = "aspect_rules_jest",
    sha256 = "098186ffc450f2a604843d8ba14217088a0e259ea6a03294af5360a7f1bcd3e8",
    strip_prefix = "rules_jest-0.19.5",
    url = "https://github.com/aspect-build/rules_jest/releases/download/v0.19.5/rules_jest-v0.19.5.tar.gz",
)

load("@aspect_rules_webpack//webpack:dependencies.bzl", "rules_webpack_dependencies")
rules_webpack_dependencies()
load("@aspect_rules_swc//swc:dependencies.bzl", "rules_swc_dependencies")
rules_swc_dependencies()
load("@aspect_rules_js//js:repositories.bzl", "rules_js_dependencies")
rules_js_dependencies()



# Fetches a SWC cli from
# https://github.com/swc-project/swc/releases
# If you'd rather compile it from source, you can use rules_rust, fetch the project,
# then register the toolchain yourself. (Note, this is not yet documented)
load("@aspect_rules_swc//swc:repositories.bzl", "LATEST_SWC_VERSION", "swc_register_toolchains")
swc_register_toolchains(
    name = "swc",
    swc_version = "v1.3.42",
)

load("@aspect_rules_ts//ts:repositories.bzl", "rules_ts_dependencies")
rules_ts_dependencies(
    # This keeps the TypeScript version in-sync with the editor, which is typically best.
    ts_version_from = "//:package.json",

    # Alternatively, you could pick a specific version, or use
    # load("@aspect_rules_ts//ts:repositories.bzl", "LATEST_TYPESCRIPT_VERSION")
    # ts_version = LATEST_TYPESCRIPT_VERSION
)


load("@aspect_rules_jest//jest:dependencies.bzl", "rules_jest_dependencies")
rules_jest_dependencies()

load("@rules_nodejs//nodejs:repositories.bzl", "nodejs_register_toolchains")

nodejs_register_toolchains(
    name = "nodejs",
    node_version = node_version,
)
load("@aspect_rules_js//npm:repositories.bzl", "npm_translate_lock")
npm_translate_lock(
    name = "npm",
    pnpm_lock = "//:pnpm-lock.yaml",
    verify_node_modules_ignored = "//:.bazelignore",
    npmrc = "//:.npmrc",
    pnpm_version = "7.25.0",
    data = [
         "//:package.json",
         "//:pnpm-workspace.yaml",
         "//web-ui:package.json",
         "//web-ui/src:package.json",
    ],
#    generate_bzl_library_targets = True,

    bins = {
        "typescript": {
            "tsc": "./bin/tsc",
            "tsserver": "./bin/tsserver",
        },
    },
    public_hoist_packages = {
#        "eslint-config-react-app": [""],
        "eslint@registry.npmmirror.com/eslint@8.0.0": [""]
    },
)

load("@npm//:repositories.bzl", "npm_repositories")

npm_repositories()


# ## sass start
# git_repository(
#    name = "io_bazel_rules_sass",
#    commit = "354793d0603dbe26232f4a5fb25e67e0e9e4c909",
#    remote = "https://github.com/bazelbuild/rules_sass.git",
# )


# # Setup Bazel NodeJS rules.
# # See: https://bazelbuild.github.io/rules_nodejs/install.html.


# # Setup repositories which are needed for the Sass rules.
# load("@io_bazel_rules_sass//:defs.bzl", "sass_repositories")
# sass_repositories()
# ## sass end


## python start

http_archive(
    name = "rules_python",
    sha256 = "a644da969b6824cc87f8fe7b18101a8a6c57da5db39caa6566ec6109f37d2141",
    strip_prefix = "rules_python-0.20.0",
    url = "https://github.com/bazelbuild/rules_python/releases/download/0.20.0/rules_python-0.20.0.tar.gz",
)

load("@rules_python//python:repositories.bzl", "py_repositories")


py_repositories()

load("@rules_python//python:repositories.bzl", "python_register_toolchains")

python_register_toolchains(
    name = "python3_11",
    # Python up to 3.11 is required by sqlfluff
    python_version = "3.11",
)

load("@python3_11//:defs.bzl", interpreter_3_11 = "interpreter")

load("@rules_python//python:pip.bzl", "pip_parse")

# Create a central repo that knows about the dependencies needed from
# requirements_lock.txt.
pip_parse(
   name = "pip_deps",
   python_interpreter_target = interpreter_3_11,
   requirements_lock = "//third_party:requirements_lock.txt",
)
# Load the starlark macro which will define your dependencies.
load("@pip_deps//:requirements.bzl", "install_deps")
## Call it to define repos for your requirements.
install_deps()

## python end


## rule_oci start
http_archive(
    name = "rules_oci",
    sha256 = "21a7d14f6ddfcb8ca7c5fc9ffa667c937ce4622c7d2b3e17aea1ffbc90c96bed",
    strip_prefix = "rules_oci-1.4.0",
    url = "https://github.com/bazel-contrib/rules_oci/releases/download/v1.4.0/rules_oci-v1.4.0.tar.gz",
)

load("@rules_oci//oci:dependencies.bzl", "rules_oci_dependencies")

rules_oci_dependencies()

load("@rules_oci//oci:repositories.bzl", "LATEST_CRANE_VERSION", "LATEST_ZOT_VERSION", "oci_register_toolchains")

oci_register_toolchains(
    name = "oci",
    crane_version = LATEST_CRANE_VERSION,
    # Uncommenting the zot toolchain will cause it to be used instead of crane for some tasks.
    # Note that it does not support docker-format images.
    # zot_version = LATEST_ZOT_VERSION,
)
load("@rules_oci//oci:pull.bzl", "oci_pull")
oci_pull(
    name = "distroless_java",
    digest = "sha256:161a1d97d592b3f1919801578c3a47c8e932071168a96267698f4b669c24c76d",
    image = "gcr.io/distroless/java17",
)

# You can pull your base images using oci_pull like this:
load("@rules_oci//oci:pull.bzl", "oci_pull")

oci_pull(
    name = "distroless_base",
    digest = "sha256:ccaef5ee2f1850270d453fdf700a5392534f8d1a8ca2acda391fbb6a06b81c86",
    image = "gcr.io/distroless/base",
    platforms = [
        "linux/amd64",
        "linux/arm64",
    ],
)

## rule_docker end

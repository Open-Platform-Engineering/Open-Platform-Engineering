import unittest
import sqlfluff
import os
import codecs

sqls_path = os.path.join(os.getcwd(), "repository-impl", "src","main","sql","domain")

dialect = "postgres"

class TestSum(unittest.TestCase):
    def test_lint_sql(self):
        sql_dir_files = os.listdir(sqls_path)
        # assert that there's a sql file at least in the dir.
        self.assertTrue(len(sql_dir_files) > 0)
        for sql_filename in sql_dir_files:
            if sql_filename.endswith(".sql"):
                f = codecs.open(os.path.join(sqls_path, sql_filename), "r", "utf-8")
                sql_content = f.read()
                lint_result = sqlfluff.lint(sql_content, dialect=dialect)
                # if there's sql issue then
                if len(lint_result) > 0:
                    # get the fixed sql content from SQLFluff
                    fix_result = sqlfluff.fix(sql_content, dialect=dialect)
                    # output the fixed sql content
                    print("correct sql should be: \n" + fix_result)
                self.assertEqual(len(lint_result), 0)

if __name__ == "__main__":
    unittest.main()
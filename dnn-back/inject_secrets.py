from re import compile, sub
from os import environ


secret_pattern = compile(r'\<\<.*?\>\>')


def replace_with_secret(secret_str):
    secret_var = secret_str[2:-2]
    if secret_var in environ:
        return environ[secret_var]
    return secret_str


def inject_secrets(config_line):
    return sub(secret_pattern, replace_with_secret, config_line)


if __name__ == "__main__":
    with open("./dnn-back/src/main/resources/application.yml", "r") as config:
        config_lines = config.readlines()
    with open("./dnn-back/src/main/resources/application.yml", "w") as config:
        config.writelines(map(inject_secrets, config_lines))

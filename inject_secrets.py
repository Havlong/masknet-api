from re import Match, compile, sub
from os import environ, getcwd
from os.path import isfile as file_exists
from sys import argv


secret_pattern = compile(r'\<\<.*?\>\>')


def replace_with_secret(secret_str: Match):
    secret_str = secret_str.group()
    secret_var = secret_str[2:-2]
    if secret_var in environ:
        return environ[secret_var]
    return secret_str


def inject_secrets(config_line: str):
    return sub(secret_pattern, replace_with_secret, config_line)


def inject_secrets_to_file(file_path: str):
    print(f'[INFO]: Injecting secrets into {file_path}')
    if not file_exists(file_path):
        print(f"[WARN]: File {file_path} doesn't exist")

    with open(file_path, "r") as config:
        config_lines = config.readlines()
    with open(file_path, "w") as config:
        config.writelines(map(inject_secrets, config_lines))

    print(f'[INFO]: Processed file {file_path}')


if __name__ == "__main__":
    print(argv[1:])
    print(getcwd())
    for file_path in argv[1:]:
        inject_secrets_to_file(file_path)

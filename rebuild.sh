#!/bin/bash

ansible-playbook --ask-vault-password -i ansible-inventory.yml -e "@env-secrets.yml" ansible-playbook.yml

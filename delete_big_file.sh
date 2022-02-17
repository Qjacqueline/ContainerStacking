#!/bin/bash
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch src/com/jacqueline/containerstackingproblem/data/dynamic\ result/storevalue_74_3.txt' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch src/com/jacqueline/containerstackingproblem/data/dynamic\ result/storevalue_75_3.txt' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch src/com/jacqueline/containerstackingproblem/data/instances/instance_18_3.txt' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch src/com/jacqueline/containerstackingproblem/data/dynamic\ result/storevalue_65_3.txt' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch src/com/jacqueline/containerstackingproblem/data/dynamic result/storevalue_74_3.txt' HEAD
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch src/com/jacqueline/containerstackingproblem/data/dynamic\result/storevalue_74_3.txt' HEAD

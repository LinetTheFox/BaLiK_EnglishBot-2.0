# Setting local env variables if present
if [[ -e .env ]]; then 
    for line in $(cat .env); do
        export $line
    done 
fi

# Running worker
sh target/bin/workerBot
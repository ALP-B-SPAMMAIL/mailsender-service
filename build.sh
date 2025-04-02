az acr login --name team04registry
docker build -t team04registry.azurecr.io/mailsender-service:latest .
docker push team04registry.azurecr.io/mailsender-service:latest
kubectl delete -f kubernetes/deployment.yaml  
kubectl apply -f kubernetes/deployment.yaml
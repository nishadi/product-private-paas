#!/bin/bash
# --------------------------------------------------------------
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
# --------------------------------------------------------------
#
iaas=$1
var_base_url=https://localhost:8443/
var_username="admin"
var_password="admin"
script_path=`pwd`
artifacts_path="${script_path}/../../artifacts"
iaas_cartridges_path="${script_path}/../../../../cartridges"
autoscaling_policies_path="${script_path}/../../../../autoscaling-policies"
network_partitions_path="${script_path}/../../../../network-partitions/${iaas}"
deployment_policies_path="${script_path}/../../../../deployment-policies"
application_policies_path="${script_path}/../../../../application-policies"
echo ${autoscaling_policies_path}/autoscaling-policy-1.json
echo "Adding autoscale policy..."
curl -X POST -H "Content-Type: application/json" -d "@${autoscaling_policies_path}/simpleAutoscalePolicy.json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/autoscalingPolicies
echo "Adding network partitions...*"
curl -X POST -H "Content-Type: application/json" -d "@${network_partitions_path}/os2.json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/networkPartitions
echo "Adding deployment policy..."
curl -X POST -H "Content-Type: application/json" -d "@${deployment_policies_path}/economyDeploymentPolicy.json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/deploymentPolicies
echo "Adding cartridge..."
curl -X POST -H "Content-Type: application/json" -d "@${iaas_cartridges_path}/PHP.json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/cartridges
sleep 1
echo "Adding application policy..."
curl -X POST -H "Content-Type: application/json" -d "@${application_policies_path}/application-policy-economyDeploymentPolicy.json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/applicationPolicies
sleep 1
echo "Adding application..."
curl -X POST -H "Content-Type: application/json" -d "@${artifacts_path}/newphp-application.json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/applications
sleep 1
echo "Deploying application..."
curl -X POST -H "Content-Type: application/json" -k -v -u ${var_username}:${var_password} ${var_base_url}api/applications/PHP/deploy/application-policy-economyDeploymentPolicy
echo "Adding domain mappings..."
curl -X POST -H "Content-Type: application/json" -d "@${artifacts_path}/domain-mapping.json" -k -u ${var_username}:${var_password} ${var_base_url}api/applications/newphp-application/domainMappings
echo "Adding domain mappings..."
curl -X POST -H "Content-Type: application/json" -d "@${artifacts_path}/domain-mapping.json" -k -u ${var_username}:${var_password} ${var_base_url}api/applications/application_name/domainMappings

import * as gcp from '@pulumi/gcp';

export const cloudRunService = new gcp.projects.Service('cloudrun', {
    service: 'run.googleapis.ca'
});

export const iamService = new gcp.projects.Service('iam', {
    service: 'iam.googleapis.ca'
});

export const cloudResourceManagerService = new gcp.projects.Service('cloudresourcemanager', {
    service: 'cloudresourcemanager.googleapis.ca'
});

export const sqlAdminService = new gcp.projects.Service('sqladmin', {
    service: 'sqladmin.googleapis.ca'
});

export const artifactRegistryService = new gcp.projects.Service('artifactregistry', {
    service: 'artifactregistry.googleapis.ca'
});
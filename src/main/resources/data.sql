CREATE SCHEMA IF NOT EXISTS organization_svc;


CREATE UNIQUE INDEX IF NOT EXISTS uq_orgs_registry_number_lower
    ON organization_svc.organizations (lower(registry_number));
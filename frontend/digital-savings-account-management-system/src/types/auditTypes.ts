export interface AuditEventResponse {
    id: number;
    timestamp: string;
    username: string;
    userEmail: string;
    action: string;
    entityType: string;
    entityId: number;
    details: string;
    severity: string;
    ipAddress?: string;
}

export interface AuditLogsResponse {
    logs: AuditEventResponse[];
    total: number;
}

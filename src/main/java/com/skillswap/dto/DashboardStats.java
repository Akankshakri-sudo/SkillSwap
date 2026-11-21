package com.skillswap.dto;

public class DashboardStats {

    private long totalSkillsOffered;
    private long totalSkillsWanted;
    private long pendingRequestsReceived;
    private long pendingRequestsSent;
    private long activeExchanges;
    private long activeMentoringSessions;
    private long completedSessions;

    public DashboardStats() {}

    public DashboardStats(long totalSkillsOffered, long totalSkillsWanted,
                          long pendingRequestsReceived, long pendingRequestsSent,
                          long activeExchanges, long activeMentoringSessions,
                          long completedSessions) {
        this.totalSkillsOffered = totalSkillsOffered;
        this.totalSkillsWanted = totalSkillsWanted;
        this.pendingRequestsReceived = pendingRequestsReceived;
        this.pendingRequestsSent = pendingRequestsSent;
        this.activeExchanges = activeExchanges;
        this.activeMentoringSessions = activeMentoringSessions;
        this.completedSessions = completedSessions;
    }

    public long getTotalSkillsOffered() { return totalSkillsOffered; }
    public void setTotalSkillsOffered(long totalSkillsOffered) { this.totalSkillsOffered = totalSkillsOffered; }

    public long getTotalSkillsWanted() { return totalSkillsWanted; }
    public void setTotalSkillsWanted(long totalSkillsWanted) { this.totalSkillsWanted = totalSkillsWanted; }

    public long getPendingRequestsReceived() { return pendingRequestsReceived; }
    public void setPendingRequestsReceived(long pendingRequestsReceived) { this.pendingRequestsReceived = pendingRequestsReceived; }

    public long getPendingRequestsSent() { return pendingRequestsSent; }
    public void setPendingRequestsSent(long pendingRequestsSent) { this.pendingRequestsSent = pendingRequestsSent; }

    public long getActiveExchanges() { return activeExchanges; }
    public void setActiveExchanges(long activeExchanges) { this.activeExchanges = activeExchanges; }

    public long getActiveMentoringSessions() { return activeMentoringSessions; }
    public void setActiveMentoringSessions(long activeMentoringSessions) { this.activeMentoringSessions = activeMentoringSessions; }

    public long getCompletedSessions() { return completedSessions; }
    public void setCompletedSessions(long completedSessions) { this.completedSessions = completedSessions; }

    public static DashboardStatsBuilder builder() { return new DashboardStatsBuilder(); }

    public static class DashboardStatsBuilder {
        private long totalSkillsOffered;
        private long totalSkillsWanted;
        private long pendingRequestsReceived;
        private long pendingRequestsSent;
        private long activeExchanges;
        private long activeMentoringSessions;
        private long completedSessions;

        public DashboardStatsBuilder totalSkillsOffered(long totalSkillsOffered) { this.totalSkillsOffered = totalSkillsOffered; return this; }
        public DashboardStatsBuilder totalSkillsWanted(long totalSkillsWanted) { this.totalSkillsWanted = totalSkillsWanted; return this; }
        public DashboardStatsBuilder pendingRequestsReceived(long pendingRequestsReceived) { this.pendingRequestsReceived = pendingRequestsReceived; return this; }
        public DashboardStatsBuilder pendingRequestsSent(long pendingRequestsSent) { this.pendingRequestsSent = pendingRequestsSent; return this; }
        public DashboardStatsBuilder activeExchanges(long activeExchanges) { this.activeExchanges = activeExchanges; return this; }
        public DashboardStatsBuilder activeMentoringSessions(long activeMentoringSessions) { this.activeMentoringSessions = activeMentoringSessions; return this; }
        public DashboardStatsBuilder completedSessions(long completedSessions) { this.completedSessions = completedSessions; return this; }

        public DashboardStats build() {
            return new DashboardStats(totalSkillsOffered, totalSkillsWanted, pendingRequestsReceived, pendingRequestsSent, activeExchanges, activeMentoringSessions, completedSessions);
        }
    }
}

package ejercicios.one;

import java.util.Date;

class StudentErasmus  extends Student {
    Internship date = new Internship();

    static class Internship {
        private Date startDate;

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        private Date endDate;

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public Date getEndDate() {
            return endDate;
        }
    }
}
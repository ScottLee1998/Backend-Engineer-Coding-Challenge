package io.lightfeather.springtemplate.model;

import lombok.Data;

@Data
public class Supervisor implements Comparable<Supervisor> {
    private Long id;
    private String phone;
    private String jurisdiction;
    private String identificationNumber;
    private String firstName;
    private String lastName;

    public boolean isJurisdictionNumeric() {
        if (this.jurisdiction == null) {
            return false;
        }
        try {
            Long.parseLong(this.jurisdiction);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String toString() {
        return this.getJurisdiction() + " - " + this.getLastName() + ", " + this.getFirstName();
    }

    public int compareTo(Supervisor supervisor) {
        if(!this.jurisdiction.equals(supervisor.getJurisdiction())) {
            return this.jurisdiction.compareTo(supervisor.getJurisdiction());
        } else if(!this.lastName.equals(supervisor.getLastName())) {
            return this.lastName.compareTo(supervisor.getLastName());
        } else {
            return this.firstName.compareTo(supervisor.getFirstName());
        }
    }
}

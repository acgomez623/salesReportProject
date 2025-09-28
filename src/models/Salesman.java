package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a salesman / vendor.
 * docType: e.g. "CC", "CE"
 * docNumber: numerical identifier for the document
 */
public class Salesman implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String docType;
    private long docNumber;
    private String firstName;
    private String lastName;

    /**
     * Create a new Salesman.
     *
     * @param docType   document type (non-null)
     * @param docNumber document number (>= 0)
     * @param firstName first name (non-null)
     * @param lastName  last name (non-null)
     */
    public Salesman(String docType, long docNumber, String firstName, String lastName) {
        this.docType = Objects.requireNonNull(docType, "docType must not be null");
        if (docNumber < 0) {
            throw new IllegalArgumentException("docNumber must be >= 0");
        }
        this.docNumber = docNumber;
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = Objects.requireNonNull(docType, "docType must not be null");
    }

    public long getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(long docNumber) {
        if (docNumber < 0) {
            throw new IllegalArgumentException("docNumber must be >= 0");
        }
        this.docNumber = docNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
    }

    /** Returns "FirstName LastName". */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /** Returns filename-friendly prefix: DocType_docNumber (matching sales files). */
    public String getSalesFileNamePrefix() {
        return docType + "_" + docNumber;
    }

    @Override
    public String toString() {
        return "Salesman{" +
               "docType='" + docType + '\'' +
               ", docNumber=" + docNumber +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salesman)) return false;
        Salesman salesman = (Salesman) o;
        return docNumber == salesman.docNumber &&
               Objects.equals(docType, salesman.docType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docType, docNumber);
    }

}

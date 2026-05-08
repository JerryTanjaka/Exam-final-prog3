package hei.fprog3.api.model;

public class MemberDescription {

    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String occupation;

    @Override
    public String toString() {
        return "MemberDescription{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}

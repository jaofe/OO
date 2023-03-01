public class Review {
    public String review;
    public String reviewerUsername;

    public Review(String a, String b){
        review = a;
        reviewerUsername = b;
    }

    public void printReview () {
        System.out.println(reviewerUsername);
        System.out.println(review);
    }
}

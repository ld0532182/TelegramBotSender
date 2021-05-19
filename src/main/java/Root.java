import java.util.List;

public class Root {

    public Response response;

    public Response getResponse() {
        return response;
    }

    public static class Response{
        public int count;
        public List<Item> items;

        public int getCount() {
            return count;
        }

        public List<Item> getItems() {
            return items;
        }
    }

    public static class Size{
        public int height;
        public String url;
        public String type;
        public int width;

        public String getUrl() {
            return url;
        }
    }

    public static class Photo{
        public int album_id;
        public int date;
        public int id;
        public int owner_id;
        public boolean has_tags;
        public String access_key;
        public List<Size> sizes;
        public String text;

        public List<Size> getSizes() {
            return sizes;
        }
    }

    public static class Attachment{
        public String type;
        public Photo photo;

        public Photo getPhoto() {
            return photo;
        }
    }

    public static class PostSource{
        public String type;
    }

    public static class Comments{
        public int count;
        public int can_post;
        public boolean groups_can_post;
    }

    public static class Likes{
        public int count;
        public int user_likes;
        public int can_like;
        public int can_publish;
    }

    public static class Reposts{
        public int count;
        public int user_reposted;
    }

    public static class Views{
        public int count;
    }

    public static class Donut{
        public boolean is_donut;
    }

    public static class Item{
        public int id;
        public int from_id;
        public int owner_id;
        public int date;
        public int marked_as_ads;
        public String post_type;
        public String text;
        public List<Attachment> attachments;
        public PostSource post_source;
        public Comments comments;
        public Likes likes;
        public Reposts reposts;
        public Views views;
        public Donut donut;
        public double short_text_rate;

        public List<Attachment> getAttachments() {
            return attachments;
        }
    }

}

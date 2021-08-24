import java.util.List;

public class JSONClass {

    public Response response;

    public Response getResponse() {
        return response;
    }

    public static class Response {
        public int count;
        public List<Item> items;

        public int getCount() {
            return count;
        }

        public List<Item> getItems() {
            return items;
        }
    }

    public static class Size {
        public String url;

        public String getUrl() {
            return url;
        }
    }

    public static class Photo {
        public List<Size> sizes;


        public List<Size> getSizes() {
            return sizes;
        }
    }

    public static class Attachment {
        public String type;
        public Photo photo;

        public String getType() {
            return type;
        }

        public Photo getPhoto() {
            return photo;
        }
    }

    public static class Item {
        public int id;
        public String text;
        public List<Attachment> attachments;


        public List<Attachment> getAttachments() {
            return attachments;
        }
    }

}

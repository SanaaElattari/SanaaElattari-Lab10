import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WordCounter {
    

    public static int processText(StringBuffer s, String stopword) throws TooSmallText, InvalidStopwordException
    {   
        // add another counter 
        int count = 0; // whole phrase word count 
        int counterToStopword = 0; // count until stopword found
        boolean found = false;
        if(stopword == null)
        {
            found = true;
        }

        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher matcher = regex.matcher(s.toString());

        
        
        while (matcher.find()) {
            String word = matcher.group();
            
            if (stopword != null && word.equals(stopword)) {
                found = true;
                break; // stop counting when stopword found
            }

            count++;
        }

        if(count < 5)
        {
            throw new TooSmallText("Only found " + count + " words.");
        }

        if (stopword != null && !found) 
        {
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }

        return count; 
    }

    //chweck this method 
    public static StringBuffer processFile(String path) throws EmptyFileException {
        Scanner input = new Scanner(System.in);
        File file = new File(path);

        while (!file.exists()) {
            System.out.println("Enter a valid filename:");
            path = input.nextLine();
            file = new File(path);
        }

        StringBuffer content = new StringBuffer();
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                content.append(reader.nextLine()).append(" ");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // probs wont reach here 
        }

        if (content.length() == 0) {
            throw new EmptyFileException(file.getName() + " was empty");
        }

        return content;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringBuffer text = new StringBuffer();
        String stopword = (args.length > 1) ? args[1] : null;
        int option = 0;

        // ask until valid
        while (option != 1 && option != 2) {
            System.out.println("Enter 1 to process a file or 2 to enter text directly:");
            try {
                option = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Try again.");
            }
        }

        try {
            if (option == 1) {
                String path = args.length > 0 ? args[0] : "";
                try {
                    text = processFile(path);
                } catch (EmptyFileException e) {
                    System.out.println(e);
                    text = new StringBuffer(""); // continue with empty string
                }
            } else {
                System.out.println("Enter your text:");
                text = new StringBuffer(input.nextLine());
            }

            try {
                int count = processText(text, stopword);
                System.out.println("Found " + count + " words.");
            } catch (InvalidStopwordException e) {
                System.out.println(e);
            } catch (TooSmallText e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        input.close();
    }
}


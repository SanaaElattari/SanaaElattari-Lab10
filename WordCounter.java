import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WordCounter {
    

    public static int processText(StringBuffer s, String stopword) throws TooSmallText, InvalidStopwordException
    {   
        int count = 0; 
        boolean found = false;

        if(stopword == null)
        {
            found = true; // if stop word don't need to look
        }

        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher matcher = regex.matcher(s.toString());

        while (matcher.find()) {

            String word = matcher.group();
            count++;
            
            if (stopword != null && word.equals(stopword) && count >= 5) {
                found = true;
                break; // stop counting when stopword found
            }
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
            if (!reader.hasNextLine()) {
                reader.close();
                throw new EmptyFileException(file.getName() + " was empty");
            }
            while (reader.hasNextLine()) {
                content.append(reader.nextLine());
                if (reader.hasNextLine()) content.append(" ");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // shouldn't happen due to loop above
        }
    
        if (content.length() == 0) {
            throw new EmptyFileException(file.getName() + " was empty");
        }
    
        return content;
    
    }

public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    StringBuffer text = new StringBuffer();
    String stopword = null; 
    int option = 0;

    if(args.length > 1)
        {
            stopword = args[1];
        }

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
            String path = "";
            if (args.length > 0) {
                path = args[0];
            }

            try {
                text = processFile(path);
                } catch (EmptyFileException e) {
                    System.out.println(e);
                    text = new StringBuffer("");
                }

            } else if (option == 2) {
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


package com.lxr.cli.nexus;

public enum NexusConstants {

    REPOSITORY{
        public String toString() {
            return "repository";
        }
    },
    GROUP {
        public String toString() {
            return "group";
        }
    } ,

    NAME{ 
        public String toString() {
            return  "name";
        }
    },
    MAVENEXTENSION { 
        public String toString() {
            return "maven.extension";
        }
    },
    SORT { 
        public String toString() {
          return "sort";
        }
    },
    DIRECTION{ 
        public String toString() {
           return "direction";
        }
    },
    MAVENCLASSIFIER{
        public String toString() {
            return "maven.classifier";
        }
    };

   

}

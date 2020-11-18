#!/bin/bash

while test "$#" -gt 0; do
    case "$1" in
        -h|--help)
            echo "generate.sh - generate a mod"
            echo " "
            echo "generate.sh [options] <mod-name>"
            echo " "
            echo "options:"
            echo "-h,  --help                    show help"
            echo "-p, --package-name=PACKAGENAME specify a package name"
            echo "-c, --class-name=CLASSNAME     specify a class name"
            echo "-n, --name=MODNAME             specify a mod name"
            exit 0
            ;;
        -p)
            shift
            if test $# -gt 0; then
                PACKAGENAME=$1
            else
                echo "no package name specified"
                exit 1
            fi
            shift
            ;;
        --package-name*)
            PACKAGENAME=`echo $1 | sed -e 's/^[^=]*=//g'`
            shift
            ;;
        -c)
            shift
            if test $# -gt 0; then
                CLASSNAME=$1
            else
                echo "no class name specified"
                exit 1
            fi
            shift
            ;;
        --class-name*)
            CLASSNAME=`echo $1 | sed -e 's/^[^=]*=//g'`
            shift
            ;;
        -n)
            shift
            if test $# -gt 0; then
                MODNAME=$1
            else
                echo "no mod name specified"
                exit 1
            fi
            shift
            ;;
        --name*)
            MODNAME=`echo $1 | sed -e 's/^[^=]*=//g'`
            shift
            ;;
        -*)
            echo "Unknown option. Use generate.sh --help to get a list of available options"
            exit 1
            ;;
        *)
            break
            ;;
    esac
done

MODID="$1"

if [[ -z "$PACKAGENAME" ]]; then
    PACKAGENAME="$(printf "$MODID" | sed 's/-//g')"
fi
if [[ -z "$CLASSNAME" ]]; then
    CLASSNAME="$(printf "$MODID" | sed 's/\(-\|^\)\([a-z]\)/\U\2/g')"
fi
if [[ -z "$MODNAME" ]]; then
    MODNAME="$(printf "$MODID" | sed 's/\(-\|^\)\([a-z]\)/\U\2/g')"
fi

printf 'New mod ID = %s\n' "$MODID"
printf 'New package name = %s\n' "$PACKAGENAME"
printf 'New class name = %s\n' "$CLASSNAME"
printf 'New mod name = %s\n' "$MODNAME"
printf '\n'

read -r -p 'Are you sure? [y/N] ' RESPONSE

if [[ ! "$RESPONSE" =~ ^[yY][eE][sS]|[yY]$ ]]; then
	printf 'Aborting.\n'
	exit 1
fi

#printf "$MODID" > ./.idea/.name

function replace() {
    FILE="$1"
    FROM="$2"
    TO="$3"

    printf '%s: %s -> %s\n' "$FILE" "$FROM" "$TO"

    sed -i "s/$FROM/$TO/g" "$FILE"
}

function move() {
    FROM="$1"
    TO="$2"

    printf '%s -> %s\n' "$FROM" "$TO"

    mv "$FROM" "$TO"
}

#replace ./.idea/.name mod-skeleton "$MODID"
replace ./.idea/modules/mod-skeleton.iml mod-skeleton "$MODID"

move ./.idea/modules/mod-skeleton.iml \
     ./.idea/modules/"$MODID".iml
move ./.idea/modules/mod-skeleton.main.iml \
     ./.idea/modules/"$MODID".main.iml
move ./.idea/modules/mod-skeleton.test.iml \
     ./.idea/modules/"$MODID".test.iml

replace ./.idea/runConfigurations/Minecraft_Client.xml mod-skeleton "$MODID"
replace ./.idea/runConfigurations/Minecraft_Server.xml mod-skeleton "$MODID"

replace ./gradle.properties mod-skeleton "$MODID"
replace ./gradle.properties io.github.bymartrixx io.github.bymartrixx."$MODID"
replace ./settings.gradle mod-skeleton "$MODID"

find ./src/main/java -type f -print0 | xargs -0 sed -i "s/.skeleton/.$PACKAGENAME/g"
find ./src/main/java -type f -print0 | xargs -0 sed -i "s/ModSkeleton/$CLASSNAME/g"
find ./src/main/java -type f -print0 | xargs -0 sed -i "s/mod-skeleton/$MODID/g"
find ./src/main/java -type f -print0 | xargs -0 sed -i "s/Mod Skeleton/$MODNAME/g"

move ./src/main/java/io/github/bymartrixx/skeleton/ModSkeleton.java \
     ./src/main/java/io/github/bymartrixx/skeleton/"$CLASSNAME".java
move ./src/main/java/io/github/bymartrixx/skeleton \
     ./src/main/java/io/github/bymartrixx/"$PACKAGENAME"

replace ./src/main/resources/fabric.mod.json mod-skeleton "$MODID"
replace ./src/main/resources/fabric.mod.json .skeleton ."$PACKAGENAME"
replace ./src/main/resources/fabric.mod.json ModSkeleton "$CLASSNAME"
replace ./src/main/resources/fabric.mod.json Mod\ Skeleton "$MODNAME"

move ./src/main/resources/assets/mod-skeleton \
     ./src/main/resources/assets/"$MODID"

rm generate.bat
rm generate.sh
rm Generate.java

package info.kgeorgiy.ja.samsikova.student;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.GroupQuery;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StudentDB implements GroupQuery {

    private Comparator<Student> STUDENTS_COMPARATOR = Comparator
            .comparing(Student::getLastName)
            .reversed()
            .thenComparing(Student::getFirstName, Comparator.reverseOrder())
            .thenComparing(Student::getId);

    private List<Group> getGroupsBySomething(Collection<Student> students, Function<Collection<Student>, List<Student>> groupper) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet()
                .stream()
                .map(x -> new Group(x.getKey(), groupper.apply(x.getValue())))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroupsBySomething(students, this::sortStudentsByName);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroupsBySomething(students, this::sortStudentsById);
    }

    private <T, C extends Collection<T>> GroupName getLargestGroupBySomething(Collection<Student> students,
                                                                              Collector<Student, ?, Map<GroupName, C>> collector,
                                                                              Comparator<String> comparator) {
        return students.stream()
                .collect(collector)
                .entrySet()
                .stream()
                .max(Map.Entry.<GroupName, C>comparingByValue(Comparator.comparing(Collection::size))
                .thenComparing(Map.Entry.comparingByKey(Comparator.comparing(GroupName::name, comparator))))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return getLargestGroupBySomething(students,
                Collectors.groupingBy(Student::getGroup),
                Comparator.naturalOrder());
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return getLargestGroupBySomething(students,
                Collectors.groupingBy(Student::getGroup, Collectors.mapping(Student::getFirstName, Collectors.toSet())),
                Comparator.reverseOrder());
    }

    private List<String> getStudentSomething(List<Student> students, Function<Student, String> function) {
        return students.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getStudentSomething(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getStudentSomething(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return students.stream()
                .map(Student::getGroup)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getStudentSomething(students, x -> x.getFirstName() + " " + x.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream()
                .map(Student::getFirstName)
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Comparator.naturalOrder())
                .map(Student::getFirstName)
                .orElse("");
    }

    private List<Student> sortStudentsBySomething(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsBySomething(students, Comparator.naturalOrder());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsBySomething(students, STUDENTS_COMPARATOR);
    }

    private List<Student> findStudentsBySomething(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream()
                .filter(predicate)
                .sorted(STUDENTS_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentsBySomething(students, x -> x.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentsBySomething(students, x -> x.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return students.stream()
                .filter(x -> x.getGroup().equals(group))
                .sorted(STUDENTS_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return students.stream()
                .filter(x -> x.getGroup().equals(group))
                .collect(
                        Collectors.toMap(
                                Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }
}

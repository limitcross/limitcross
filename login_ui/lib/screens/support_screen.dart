import 'package:flutter/material.dart';

import '../widgets/mobile_frame.dart';

class SupportScreen extends StatefulWidget {
  const SupportScreen({super.key});

  @override
  State<SupportScreen> createState() => _SupportScreenState();
}

class _SupportScreenState extends State<SupportScreen> {
  final _searchController = TextEditingController();
  String _query = '';

  final _topics = const [
    _SupportTopic(Icons.calendar_month_outlined, 'Manage a booking', 'Reschedule, cancel, or track a service'),
    _SupportTopic(Icons.payments_outlined, 'Payments and refunds', 'Invoices, payment methods, and refunds'),
    _SupportTopic(Icons.person_outline, 'Account and profile', 'Update your details and preferences'),
    _SupportTopic(Icons.verified_user_outlined, 'Safety and quality', 'Our professional and service standards'),
  ];

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final filteredTopics = _topics.where((topic) {
      final text = '${topic.title} ${topic.subtitle}'.toLowerCase();
      return _query.isEmpty || text.contains(_query.toLowerCase());
    }).toList();

    return MobileFrame(
      child: Scaffold(
        backgroundColor: const Color(0xFFF8FAFC),
        appBar: AppBar(title: const Text('Help & support')),
        body: ListView(
          padding: const EdgeInsets.fromLTRB(16, 8, 16, 24),
          children: [
            Container(
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.primary,
                borderRadius: BorderRadius.circular(22),
              ),
              child: const Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Icon(Icons.support_agent_rounded, color: Colors.white, size: 32),
                  SizedBox(height: 16),
                  Text('How can we help?', style: TextStyle(color: Colors.white, fontSize: 22, fontWeight: FontWeight.w800)),
                  SizedBox(height: 6),
                  Text('Find answers or talk to our support team.', style: TextStyle(color: Color(0xFFDCE7FF), fontSize: 13)),
                ],
              ),
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _searchController,
              onChanged: (value) => setState(() => _query = value.trim()),
              decoration: InputDecoration(
                hintText: 'Search help topics',
                prefixIcon: const Icon(Icons.search_rounded),
                suffixIcon: _query.isEmpty
                    ? null
                    : IconButton(
                        tooltip: 'Clear search',
                        onPressed: () {
                          _searchController.clear();
                          setState(() => _query = '');
                        },
                        icon: const Icon(Icons.close),
                      ),
              ),
            ),
            const SizedBox(height: 24),
            const Text('Browse help topics', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w800, color: Color(0xFF0F172A))),
            const SizedBox(height: 10),
            if (filteredTopics.isEmpty)
              const Padding(
                padding: EdgeInsets.symmetric(vertical: 28),
                child: Center(child: Text('No help topics found', style: TextStyle(color: Color(0xFF64748B)))),
              )
            else
              ...filteredTopics.map(
                (topic) => Card(
                  margin: const EdgeInsets.only(bottom: 8),
                  elevation: 0,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(16),
                    side: const BorderSide(color: Color(0xFFE2E8F0)),
                  ),
                  child: ListTile(
                    contentPadding: const EdgeInsets.symmetric(horizontal: 14, vertical: 4),
                    leading: CircleAvatar(
                      backgroundColor: Theme.of(context).colorScheme.primaryContainer,
                      child: Icon(topic.icon, color: Theme.of(context).colorScheme.primary, size: 20),
                    ),
                    title: Text(topic.title, style: const TextStyle(fontWeight: FontWeight.w700, fontSize: 14)),
                    subtitle: Text(topic.subtitle, style: const TextStyle(fontSize: 12)),
                    trailing: const Icon(Icons.chevron_right_rounded),
                    onTap: () => _showTopic(context, topic),
                  ),
                ),
              ),
            const SizedBox(height: 18),
            const Text('Need a person?', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w800, color: Color(0xFF0F172A))),
            const SizedBox(height: 10),
            Row(
              children: [
                Expanded(
                  child: OutlinedButton.icon(
                    onPressed: () => _showContact(context, 'Chat with support'),
                    icon: const Icon(Icons.chat_bubble_outline_rounded, size: 18),
                    label: const Text('Chat with us'),
                  ),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: FilledButton.icon(
                    onPressed: () => _showContact(context, 'Call support'),
                    icon: const Icon(Icons.call_outlined, size: 18),
                    label: const Text('Call support'),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 18),
            const Center(
              child: Text('Support is available every day, 8 AM - 10 PM', style: TextStyle(fontSize: 11, color: Color(0xFF94A3B8))),
            ),
          ],
        ),
      ),
    );
  }

  void _showTopic(BuildContext context, _SupportTopic topic) {
    showModalBottomSheet<void>(
      context: context,
      showDragHandle: true,
      builder: (context) => SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(20, 0, 20, 24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(topic.title, style: Theme.of(context).textTheme.titleLarge),
              const SizedBox(height: 8),
              Text(topic.subtitle, style: const TextStyle(color: Color(0xFF64748B))),
              const SizedBox(height: 18),
              const Text('This help article will be connected to your Spring Boot content API.', style: TextStyle(fontSize: 13, height: 1.4)),
            ],
          ),
        ),
      ),
    );
  }

  void _showContact(BuildContext context, String action) {
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('$action will connect to Spring Boot support APIs')));
  }
}

class _SupportTopic {
  const _SupportTopic(this.icon, this.title, this.subtitle);

  final IconData icon;
  final String title;
  final String subtitle;
}
